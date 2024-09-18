package com.assgn.yourssu.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Map;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/auth/login";//"/api/login"으로 오는 요청 처리
    private static final String HTTP_METHOD = "POST";// 로그인 HTTP 메소드 타입
    private static final String CONTENT_TYPE = "application/json";// JSON 타입의 데이터만 로그인 요청 처리
    private static final String USERNAME_KEY = "email";//회원 로그인 시 이메일 요청
    private static final String PASSWORD_KEY = "password";//회원 로그인 시 비밀번호 요청
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);

    private final ObjectMapper objectMapper;


    public CustomAuthenticationFilter(ObjectMapper objectMapper){
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

    protected CustomAuthenticationFilter(String defaultFilterProcessesUrl, ObjectMapper objectMapper) {
        super(defaultFilterProcessesUrl);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)){
            throw new AuthenticationServiceException("Authentication Content-Type 이 제공되지 않습니다: "+ request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);    // 요청 바디 반환메서드 get~~

        //Map의 Key(email, password)로 해당 이메일, 패스워드 추출
        Map<String, String> emailPasswordMap = objectMapper.readValue(messageBody, Map.class);

        String email = emailPasswordMap.get(USERNAME_KEY);
        String password = emailPasswordMap.get(PASSWORD_KEY);

        //principal(이메일), credentials(비밀번호) 전달
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

        // 생성된 UsernamePasswordAuthenticationToken을 AuthenticationManager에게 넘겨 인증을 시도
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
