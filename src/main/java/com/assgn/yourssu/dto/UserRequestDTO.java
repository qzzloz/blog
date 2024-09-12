package com.assgn.yourssu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class UserRequestDTO {

    @Getter
    public static class JoinDTO{
        @NotBlank(message = "null 또는 공백이 입력되었습니다.")
        String email;

        @NotNull
        String password;

        @NotNull
        String username;

    }
}
