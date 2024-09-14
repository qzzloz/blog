package com.assgn.yourssu.controller;

import com.assgn.yourssu.domain.common.ApiResponse;
import com.assgn.yourssu.dto.ArticleRequestDTO;
import com.assgn.yourssu.dto.ArticleResponseDTO;
import com.assgn.yourssu.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("")
    @Operation(summary = "새 글 작성", description = "새로운 글을 작성합니다.")
    public ApiResponse<ArticleResponseDTO.ArticleDTO> createArticle(@RequestBody @Valid ArticleRequestDTO.CreateArticleDTO request) {
        ArticleResponseDTO.ArticleDTO response = articleService.createArticle(request);
        return ApiResponse.of("게시글 등록 완료", response);
    }

    @GetMapping("/{article_id}")
    @Operation(summary = "게시글 조회", description = "게시글을 조회합니다.")
    public ApiResponse<ArticleResponseDTO.GetArticleDTO> getArticle(@PathVariable Long article_id) {
        ArticleResponseDTO.GetArticleDTO response = articleService.getArticle(article_id);
        return ApiResponse.of("게시글 조회 성공", response);
    }

    @PutMapping(value = "/{article_id}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다. 자신의 게시글만 수정할 수 있습니다.")
    public ApiResponse<ArticleResponseDTO.ArticleDTO> updateArticle(@PathVariable Long article_id,
                                                         @RequestBody @Valid ArticleRequestDTO.UpdateArticleDTO request) {
        ArticleResponseDTO.ArticleDTO response = articleService.updateArticle(article_id, request);
        return ApiResponse.of("게시글 수정 성공", response);
    }

    @DeleteMapping(value = "/{article_id}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다. 자신의 게시글만 삭제할 수 있습니다.")
    public ApiResponse<Void> deleteArticle(@PathVariable Long article_id,
                                           @RequestBody @Valid ArticleRequestDTO.DeleteArticleDTO request) {
        articleService.deleteArticle(article_id, request);
        return ApiResponse.onSuccess(null);
    }

}
