package com.assgn.yourssu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDTO {
    private Long id;
    private String email;
    private String title;
    private String content;
}
