package com.assgn.yourssu.exception.handler;

import com.assgn.yourssu.domain.User;
import com.assgn.yourssu.domain.common.ErrorStatus;
import com.assgn.yourssu.exception.UserException;
import com.assgn.yourssu.repository.UserRepository;
import com.assgn.yourssu.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler { // 로그에 찍힘

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository; ;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = authentication.getName();
        userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_EXIST));

        // 인증 성공 후 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        log.info("로그인 성공. 이메일 : {}", email);
        log.info("로그인 성공. AccessToken: {}", accessToken);

        // 헤더에 토큰 포함
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh-Token", refreshToken);

        // 성공 응답
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
