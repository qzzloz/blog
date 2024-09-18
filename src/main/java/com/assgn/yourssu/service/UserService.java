package com.assgn.yourssu.service;

import com.assgn.yourssu.domain.common.ErrorStatus;
import com.assgn.yourssu.dto.TokenDTO;
import com.assgn.yourssu.dto.UserRequestDTO;
import com.assgn.yourssu.dto.UserResponseDTO;
import com.assgn.yourssu.domain.User;
import com.assgn.yourssu.exception.UserException;
import com.assgn.yourssu.repository.UserRepository;
import com.assgn.yourssu.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserResponseDTO.JoinResponseDTO join(UserRequestDTO.JoinDTO request){
        // 이미 가입한 회원인지 검사
        userRepository.findByEmail(request.getEmail()).ifPresent(
                user -> {
                    throw new UserException(ErrorStatus.USER_ALREADY_EXIST);
                }
        );

        // 방법 2
//        User user = request.toEntity(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getUsername());

        User user = userRepository.save(User.builder()
                        .email(request.getEmail())
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build());

        return UserResponseDTO.JoinResponseDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    // TODO: 단순 검사말고 로그인 기능으로 바꾸기
    public User checkEmailPwd(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_EXIST));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new UserException(ErrorStatus.NOT_VALID_PASSWORD);
        }

        return user;
    }


    @Transactional
    public void deleteUser(UserRequestDTO.WithdrawDTO request) {
        User user = checkEmailPwd(request.getEmail(), request.getPassword());
        userRepository.delete(user);
    }

    @Transactional
    public TokenDTO signIn(UserRequestDTO.SigninDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        return jwtTokenProvider.createToken(authentication);

    }
}
