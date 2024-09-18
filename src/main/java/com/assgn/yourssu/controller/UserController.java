package com.assgn.yourssu.controller;

import com.assgn.yourssu.domain.common.ApiResponse;
import com.assgn.yourssu.dto.TokenDTO;
import com.assgn.yourssu.dto.UserRequestDTO;
import com.assgn.yourssu.dto.UserResponseDTO;
import com.assgn.yourssu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입")
    public ResponseEntity<UserResponseDTO.JoinResponseDTO> join(@RequestBody @Valid UserRequestDTO.JoinDTO request) {
        UserResponseDTO.JoinResponseDTO response = userService.join(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/withdraw")
    @Operation(summary = "회원 탈퇴", description = "해당 회원이 작성한 게시글, 댓글이 모두 삭제됩니다.")
    public ApiResponse<Void> withdraw(@RequestBody @Valid UserRequestDTO.WithdrawDTO request) {
        userService.deleteUser(request);
        return ApiResponse.onSuccess(null);
    }

//    @PostMapping("/signin")
//    public ResponseEntity<TokenDTO> signIn(@RequestBody @Valid UserRequestDTO.SigninDTO request){
//        TokenDTO token = userService.signIn(request);
//        return ResponseEntity.ok(token);
//
//    }
}
