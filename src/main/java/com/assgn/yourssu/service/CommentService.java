package com.assgn.yourssu.service;

import com.assgn.yourssu.domain.Article;
import com.assgn.yourssu.domain.Comment;
import com.assgn.yourssu.domain.User;
import com.assgn.yourssu.domain.common.ErrorStatus;
import com.assgn.yourssu.dto.CommentRequestDTO;
import com.assgn.yourssu.dto.CommentResponseDTO;
import com.assgn.yourssu.exception.ArticleException;
import com.assgn.yourssu.exception.CommentException;
import com.assgn.yourssu.repository.ArticleRepository;
import com.assgn.yourssu.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDTO createComment(Long articleId, CommentRequestDTO.CreateCommentDTO request) {

        User writer = userService.checkEmailPwd(request.getEmail(), request.getPassword());

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException(ErrorStatus.ARTICLE_NOT_EXITS));

        Comment newComment = Comment.builder()
                .article(article)
                .content(request.getContent())
                .user(writer)
                .build();

        Comment savedComment = commentRepository.save(newComment);

        return CommentResponseDTO.builder()
                .commentId(savedComment.getId())
                .email(savedComment.getUser().getEmail())
                .content(savedComment.getContent())
                .build();
    }

    @Transactional
    public CommentResponseDTO getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(ErrorStatus.COMMENT_NOT_EXIST));

        return CommentResponseDTO.builder()
                .commentId(comment.getId())
                .email(comment.getUser().getEmail())
                .content(comment.getContent())
                .build();
    }

    @Transactional
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO.UpdateCommentDTO request) {

        User writer = userService.checkEmailPwd(request.getEmail(), request.getPassword());

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(ErrorStatus.COMMENT_NOT_EXIST));

        if(Objects.equals(writer.getId(), comment.getUser().getId())) {
            comment.update(request.getContent());
        }else{
            throw new CommentException(ErrorStatus.NOT_VALID_USER);
        }

        return CommentResponseDTO.builder()
                .commentId(commentId)
                .email(comment.getUser().getEmail())
                .content(comment.getContent())
                .build();
    }

    @Transactional
    public void deleteComment(Long commentId, CommentRequestDTO.DeleteCommentDTO request) {
        User writer = userService.checkEmailPwd(request.getEmail(), request.getPassword());

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(ErrorStatus.COMMENT_NOT_EXIST));

        if(Objects.equals(writer.getId(), comment.getUser().getId())) {
            commentRepository.delete(comment);
        }else{
            throw new CommentException(ErrorStatus.NOT_VALID_USER);
        }
    }
}
