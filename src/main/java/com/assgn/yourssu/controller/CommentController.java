package com.assgn.yourssu.controller;

import com.assgn.yourssu.domain.common.ApiResponse;
import com.assgn.yourssu.dto.CommentRequestDTO;
import com.assgn.yourssu.dto.CommentResponseDTO;
import com.assgn.yourssu.security.CustomUserDetails;
import com.assgn.yourssu.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{article_id}")
    @Operation(summary = "새 댓글 작성", description = "새로운 댓글을 작성합니다.")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable("article_id") Long articleId,
                                                            @RequestBody CommentRequestDTO.CreateCommentDTO request,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        CommentResponseDTO response = commentService.createComment(articleId, request, userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{comment_id}")
    @Operation(summary = "댓글 조회", description = "댓글을 조회합니다.")
    public ResponseEntity<CommentResponseDTO> getComment(@PathVariable("comment_id") Long commentId) {
        CommentResponseDTO response = commentService.getComment(commentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{comment_id}")
    @Operation(summary = "댓글 수정", description = "자신이 작성한 댓글만 수정할 수 있습니다.")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable("comment_id") Long commentId,
                                                            @RequestBody CommentRequestDTO.UpdateCommentDTO request,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails){
        CommentResponseDTO response = commentService.updateComment(commentId, request, userDetails);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{comment_id}")
    @Operation(summary = "댓글 삭제", description = "자신이 작성한 댓긂만 삭제할 수 있습니다.")
    public ApiResponse<Void> deleteComment(@PathVariable("comment_id") Long commentId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails);
        return ApiResponse.onSuccess(null);
    }
}
