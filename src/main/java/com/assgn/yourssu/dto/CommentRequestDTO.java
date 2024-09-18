package com.assgn.yourssu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class CommentRequestDTO {

    @Getter
    public static class CreateCommentDTO {
        @NotBlank
        private String content;
    }

    @Getter
    public static class UpdateCommentDTO {
        @NotBlank
        private String content;
    }
}
