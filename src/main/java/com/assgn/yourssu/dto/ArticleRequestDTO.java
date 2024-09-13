package com.assgn.yourssu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class ArticleRequestDTO {

    @Getter
    public static class CreateArticleDTO {
        private String email;
        private String password;

        @NotBlank
        private String title;

        @NotBlank
        private String content;
    }

    @Getter
    public static class UpdateArticleDTO {
        private String email;
        private String password;

        @NotBlank(message = "null 또는 공백이 입력되었습니다.")
        private String title;

        @NotBlank(message = "null 또는 공백이 입력되었습니다.")
        private String content;
    }

    @Getter
    public static class DeleteArticleDTO {
        private String email;
        private String password;
    }
}
