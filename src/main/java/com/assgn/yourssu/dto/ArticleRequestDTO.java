package com.assgn.yourssu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class ArticleRequestDTO {

    @Getter
    public static class CreateArticleDTO {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }

    @Getter
    public static class UpdateArticleDTO {
        @NotBlank
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }

    @Getter
    public static class DeleteArticleDTO {
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }
}
