package com.assgn.yourssu.dto;

import com.assgn.yourssu.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDTO {
    private Long id;
    private String email;
    private String title;
    private String content;
    private List<CommentResponseDTO> commentList;
}
