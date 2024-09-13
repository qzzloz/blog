package com.assgn.yourssu.controller;

import com.assgn.yourssu.domain.common.ApiResponse;
import com.assgn.yourssu.dto.CommentRequestDTO;
import com.assgn.yourssu.dto.CommentResponseDTO;
import com.assgn.yourssu.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{article_id}")
    @Operation(summary = "새 댓글 작성", description = "새로운 댓글을 작성합니다.")
    public ApiResponse<CommentResponseDTO> createComment(@PathVariable("article_id") Long articleId,
                                                      @RequestBody CommentRequestDTO.CreateCommentDTO request) {
        CommentResponseDTO response = commentService.createComment(articleId, request);
        return ApiResponse.of("댓글 업로드 성공", response);
    }

    @GetMapping("/{comment_id}")
    @Operation(summary = "댓글 조회", description = "댓글을 조회합니다.")
    public ApiResponse<CommentResponseDTO> getComment(@PathVariable("comment_id") Long commentId) {
        CommentResponseDTO response = commentService.getComment(commentId);
        return ApiResponse.of("댓글 조회", response);
    }

    @PutMapping("/{comment_id}")
    @Operation(summary = "댓글 수정", description = "자신이 작성한 댓글만 수정할 수 있습니다.")
    public ApiResponse<CommentResponseDTO> updateComment(@PathVariable("comment_id") Long commentId,
                                                         @RequestBody CommentRequestDTO.UpdateCommentDTO request){
        CommentResponseDTO response = commentService.updateComment(commentId, request);
        return ApiResponse.of("댓글 수정 완료", response);
    }

    @DeleteMapping("/{comment_id}")
    @Operation(summary = "댓글 삭제", description = "자신이 작성한 댓긂만 삭제할 수 있습니다.")
    public ApiResponse<Void> deleteComment(@PathVariable("comment_id") Long commentId,
                                           @RequestBody CommentRequestDTO.DeleteCommentDTO request) {
        commentService.deleteComment(commentId, request);
        return ApiResponse.onSuccess(null);
    }
}
