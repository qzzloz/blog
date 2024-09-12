package com.assgn.yourssu.controller;

import com.assgn.yourssu.domain.common.ApiResponse;
import com.assgn.yourssu.dto.UserRequestDTO;
import com.assgn.yourssu.dto.UserResponseDTO;
import com.assgn.yourssu.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ApiResponse<UserResponseDTO.JoinResponseDTO> join(@RequestBody @Valid UserRequestDTO.JoinDTO request) {
        // TODO: 이메일 공백, null 입력 검사
        UserResponseDTO.JoinResponseDTO response = userService.join(request);
        return ApiResponse.of("회원가입 성공", response);
    }
}
