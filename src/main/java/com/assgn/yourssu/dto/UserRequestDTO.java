package com.assgn.yourssu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class UserRequestDTO {

    @Getter
    public static class JoinDTO{
        @NotBlank
        String email;
        @NotNull
        String password;
        @NotNull
        String username;
    }

    @Getter
    public static class WithdrawDTO{
        @NotBlank
        String email;
        @NotBlank
        String password;
    }

    @Getter
    public static class SigninDTO{
        @NotBlank
        String email;
        @NotBlank
        String password;
    }
}
