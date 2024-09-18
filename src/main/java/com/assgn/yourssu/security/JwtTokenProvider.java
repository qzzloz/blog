package com.assgn.yourssu.security;

import com.assgn.yourssu.dto.TokenDTO;
import com.assgn.yourssu.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    public TokenDTO createToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        String accessToken = createAccessToken(email);
        String refreshToken = createRefreshToken();

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)      // jwt의 이름
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))   // jwt 만료시간
                .withClaim(EMAIL_CLAIM, email)      // 토큰의 payload, 키-값 형태
                .sign(Algorithm.HMAC512(secretKey));    // 사용 해싱 알고리, 시크릿키
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)      // jwt의 이름
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))   // jwt 만료시간
                .sign(Algorithm.HMAC512(secretKey));    // 사용 해싱 알고리, 시크릿키
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessHeader -> accessHeader.startsWith(BEARER))
                .map(accessHeader -> accessHeader.replace(BEARER, ""));
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        }catch (Exception e){
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    /**
     * AccessToken에서 Email 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 이메일 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractEmail(String accessToken){
        try{
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)//accessToken 유효한지 검증
                    .getClaim(EMAIL_CLAIM)//claim에 담긴 email 가져오기
                    .asString());

        }catch (Exception e){
            log.error("엑세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public Authentication getAuthentication(String token) {
        // JWT에서 사용자 정보를 추출
        String email = extractEmail(token).orElse(null);
        if (email != null) {
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
            if (userDetails != null) {
                return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
            }
        }
        return null; // 이메일이 없거나 사용자 정보가 없을 경우 null 반환
    }
}
