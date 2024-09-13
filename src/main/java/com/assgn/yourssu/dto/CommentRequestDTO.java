package com.assgn.yourssu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class CommentRequestDTO {

    @Getter
    public static class CreateCommentDTO {
        private String email;
        private String password;

        @NotBlank
        private String content;
    }

    @Getter
    public static class UpdateCommentDTO {
        private String email;
        private String password;


        @NotBlank
        private String content;
    }

    @Getter
    public static class DeleteCommentDTO {
        private String email;
        private String password;
    }
}
