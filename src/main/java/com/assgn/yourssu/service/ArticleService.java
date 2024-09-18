package com.assgn.yourssu.service;

import com.assgn.yourssu.domain.Article;
import com.assgn.yourssu.domain.User;
import com.assgn.yourssu.domain.common.ErrorStatus;
import com.assgn.yourssu.dto.ArticleRequestDTO;
import com.assgn.yourssu.dto.ArticleResponseDTO;
import com.assgn.yourssu.dto.CommentResponseDTO;
import com.assgn.yourssu.exception.ArticleException;
import com.assgn.yourssu.exception.UserException;
import com.assgn.yourssu.repository.ArticleRepository;
import com.assgn.yourssu.repository.UserRepository;
import com.assgn.yourssu.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;

    @Transactional
    public ArticleResponseDTO.ArticleDTO createArticle(ArticleRequestDTO.CreateArticleDTO request,
                                                       CustomUserDetails customUserDetails) {
        User writer = customUserDetails.getUser();

        Article newArticle = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(writer)
                .build();

        Article savedArticle = articleRepository.save(newArticle);

        return ArticleResponseDTO.ArticleDTO.builder()
                .articleId(savedArticle.getId())
                .title(savedArticle.getTitle())
                .content(savedArticle.getContent())
                .email(savedArticle.getUser().getEmail())
                .build();
    }

    @Transactional
    public ArticleResponseDTO.GetArticleDTO getArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(ErrorStatus.ARTICLE_NOT_EXITS));

        List<CommentResponseDTO> commentList = article.getCommentList().stream()
                .map(comment -> CommentResponseDTO.builder()
                        .commentId(comment.getId())
                        .email(comment.getUser().getEmail())
                        .content(comment.getContent())
                        .build())
                .collect(Collectors.toList());

        return ArticleResponseDTO.GetArticleDTO.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .email(article.getUser().getEmail())
                .commentList(commentList)
                .build();
    }

    @Transactional
    public ArticleResponseDTO.ArticleDTO updateArticle(Long articleId, ArticleRequestDTO.UpdateArticleDTO request) {
        User writer = userService.checkEmailPwd(request.getEmail(), request.getPassword());

        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(ErrorStatus.ARTICLE_NOT_EXITS));

        if(Objects.equals(writer.getId(), article.getUser().getId())) {
            article.update(request.getTitle(), request.getContent());
        }else{
            throw new ArticleException(ErrorStatus.NOT_VALID_USER);
        }

        return ArticleResponseDTO.ArticleDTO.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .email(article.getUser().getEmail())
                .build();
    }

    @Transactional
    public void deleteArticle(Long articleId, ArticleRequestDTO.DeleteArticleDTO request) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(ErrorStatus.ARTICLE_NOT_EXITS));

        User writer = userService.checkEmailPwd(request.getEmail(), request.getPassword());

        if(Objects.equals(writer.getId(), article.getUser().getId())) {
            articleRepository.delete(article);
        }else{
            throw new ArticleException(ErrorStatus.NOT_VALID_USER);
        }
    }

}
