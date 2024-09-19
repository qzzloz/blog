package com.assgn.yourssu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ArticleRequestDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateArticleDTO {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateArticleDTO {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }
}
