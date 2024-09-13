package com.assgn.yourssu.service;

import com.assgn.yourssu.domain.Article;
import com.assgn.yourssu.domain.User;
import com.assgn.yourssu.domain.common.ErrorStatus;
import com.assgn.yourssu.dto.ArticleRequestDTO;
import com.assgn.yourssu.dto.ArticleResponseDTO;
import com.assgn.yourssu.exception.ArticleException;
import com.assgn.yourssu.exception.UserException;
import com.assgn.yourssu.repository.ArticleRepository;
import com.assgn.yourssu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleResponseDTO createArticle(ArticleRequestDTO.CreateArticleDTO request) {

        User writer = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_EXIST));

        Article newArticle = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(writer)
                .build();

        Article savedArticle = articleRepository.save(newArticle);

        return ArticleResponseDTO.builder()
                .id(savedArticle.getId())
                .title(savedArticle.getTitle())
                .content(savedArticle.getContent())
                .email(savedArticle.getUser().getEmail())
                .build();
    }

    public ArticleResponseDTO getArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(ErrorStatus.ARTICLE_NOT_EXITS));

        return ArticleResponseDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .email(article.getUser().getEmail())
                .build();
    }

    public ArticleResponseDTO updateArticle(Long articleId, ArticleRequestDTO.UpdateArticleDTO request) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(ErrorStatus.ARTICLE_NOT_EXITS));

        // TODO: request의 이메일, 비밀번호 쌍이 맞는지 검사 (로그인)
        User writer = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_EXIST));

        if(Objects.equals(writer.getId(), article.getUser().getId())) {
            article.update(request.getTitle(), request.getContent());
        }else{
            throw new ArticleException(ErrorStatus.NOT_VALID_USER);
        }

        return ArticleResponseDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .email(article.getUser().getEmail())
                .build();
    }

    public void deleteArticle(Long articleId, ArticleRequestDTO.DeleteArticleDTO request) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleException(ErrorStatus.ARTICLE_NOT_EXITS));

        // TODO: request의 이메일, 비밀번호 쌍이 맞는지 검사 (로그인)
        User writer = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_EXIST));

        if(Objects.equals(writer.getId(), article.getUser().getId())) {
            articleRepository.delete(article);
        }else{
            throw new ArticleException(ErrorStatus.NOT_VALID_USER);
        }
    }
}
