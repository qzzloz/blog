package com.assgn.yourssu.dto;

import com.assgn.yourssu.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class ArticleResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticleDTO {
        private Long articleId;
        private String email;
        private String title;
        private String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetArticleDTO {
        private Long articleId;
        private String email;
        private String title;
        private String content;
        private List<CommentResponseDTO> commentList;
    }

}
