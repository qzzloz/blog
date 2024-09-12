package com.assgn.yourssu.service;

import com.assgn.yourssu.domain.common.ErrorStatus;
import com.assgn.yourssu.dto.UserRequestDTO;
import com.assgn.yourssu.dto.UserResponseDTO;
import com.assgn.yourssu.domain.User;
import com.assgn.yourssu.exception.UserException;
import com.assgn.yourssu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO.JoinResponseDTO join(UserRequestDTO.JoinDTO request){
        // 이미 가입한 회원인지 검사
        userRepository.findByEmail(request.getEmail()).ifPresent(
                user -> {
                    throw new UserException(ErrorStatus.USER_ALREADY_EXIST);
                }
        );


        // 방법 2
//        User user = request.toEntity(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getUsername());

        // 방법 3
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


}
