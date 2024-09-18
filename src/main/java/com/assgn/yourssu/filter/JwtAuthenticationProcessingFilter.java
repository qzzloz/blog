package com.assgn.yourssu.filter;

import com.assgn.yourssu.domain.User;
import com.assgn.yourssu.repository.UserRepository;
import com.assgn.yourssu.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/api/auth/login",
                "/signin",
                "/join",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
        };
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        System.out.println("Processing request: " + request.getRequestURI());
//        String accessToken = jwtTokenProvider.extractAccessToken(request)
//                .filter(jwtTokenProvider::isTokenValid)
//                .orElse(null);
//        System.out.println("Access token: " + accessToken);

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response); // excludePath 경로의 요청이 들어오면, 다음 필터 호출
            return; //return은 필터 진행 막는 역할을 함
        }

        /**
         * 요청 헤더에서 RefreshToken 추출 이후 헤더에 RefreshToken이 없거나 DB에 저장된 값과 다르면 null을 반환
         * 요청 헤더에 RefreshToken 이 있다 -> AccessToken이 만료되어 요청한 경우
         * 위 경우를 제외하면 추출한 refreshToken은 모두 null이다.
         * */
        String refreshToken = jwtTokenProvider.extractRefreshToken(request)
                .filter(jwtTokenProvider::isTokenValid)
                .orElse(null);

        //RefreshToken이 없거나 유효하지 않으면 AccessToken 검사하고 인증
        //만약 AccessToken 없거나 유효하지 않으면 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가 403 발생
        //AccessToken 유효하면, 인증 객체가 담긴 상태로 다음 필터로 넘어가므로 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    /**
     * 액세스 토큰 체크 & 인증 처리 메서드
     * request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
     * 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
     * 그 유저 객체를 saveAuthentication()으로 인증 처리하여
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
     * 그 후 다음 인증 필터로 진행
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> accessTokenOpt = jwtTokenProvider.extractAccessToken(request);
            if (accessTokenOpt.isEmpty()) {
                log.warn("Access token not present");
                filterChain.doFilter(request, response);
                return;
            }

            String accessToken = accessTokenOpt.get();
            if (!jwtTokenProvider.isTokenValid(accessToken)) {
                log.warn("Invalid access token");
                filterChain.doFilter(request, response);
                return;
            }

            Optional<String> emailOpt = jwtTokenProvider.extractEmail(accessToken);
            if (emailOpt.isEmpty()) {
                log.warn("Email extraction failed from token");
                filterChain.doFilter(request, response);
                return;
            }

            String email = emailOpt.get();
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                log.warn("User not found for email: {}", email);
                filterChain.doFilter(request, response);
                return;
            }

            authenticateUser(userOpt.get(), accessToken);

            log.info("User authenticated successfully: {}", email);
        } catch (Exception e) {
            log.error("Error during authentication process", e);
        }

        filterChain.doFilter(request, response);
    }

    // 사용자 인증 로직 분리
    private void authenticateUser(User user, String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * [인증 허가 메서드]
     * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     *
     * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
     * UsernamePasswordAuthenticationToken의 파라미터
     * 1. 위에서 만든 UserDetailsUser 객체 (유저 정보)
     * 2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거)
     * 3. Collection < ?extends GrantedAuthority>로,
     * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에,
     * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
     *
     * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
     * setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
     */
    public void saveAuthentication(User myUser) {
        String password = myUser.getPassword();

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser, null, authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
