package com.assgn.yourssu.service;

import com.assgn.yourssu.domain.common.ErrorStatus;
import com.assgn.yourssu.dto.UserRequestDTO;
import com.assgn.yourssu.dto.UserResponseDTO;
import com.assgn.yourssu.domain.User;
import com.assgn.yourssu.exception.UserException;
import com.assgn.yourssu.repository.UserRepository;
import com.assgn.yourssu.jwt.JwtTokenProvider;
import com.assgn.yourssu.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Transactional
    public void deleteUser(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        userRepository.delete(user);
    }

//    @Transactional
//    public TokenDTO signIn(UserRequestDTO.SigninDTO request) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//
//        return jwtTokenProvider.createToken(authentication);
//
//    }
}
