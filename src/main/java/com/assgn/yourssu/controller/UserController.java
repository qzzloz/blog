package com.assgn.yourssu.controller;

import com.assgn.yourssu.domain.common.ApiResponse;
import com.assgn.yourssu.dto.UserRequestDTO;
import com.assgn.yourssu.dto.UserResponseDTO;
import com.assgn.yourssu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "회원가입", description = "회원가입")
    public ApiResponse<UserResponseDTO.JoinResponseDTO> join(@RequestBody @Valid UserRequestDTO.JoinDTO request) {
        UserResponseDTO.JoinResponseDTO response = userService.join(request);
        return ApiResponse.of("회원가입 성공", response);
    }
}
