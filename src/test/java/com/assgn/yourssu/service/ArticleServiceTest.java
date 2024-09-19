package com.assgn.yourssu.service;

import com.assgn.yourssu.domain.Article;
import com.assgn.yourssu.domain.User;
import com.assgn.yourssu.domain.common.ErrorStatus;
import com.assgn.yourssu.dto.ArticleRequestDTO;
import com.assgn.yourssu.dto.ArticleResponseDTO;
import com.assgn.yourssu.exception.ArticleException;
import com.assgn.yourssu.repository.ArticleRepository;
import com.assgn.yourssu.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CustomUserDetails customUserDetails;

    private User user;
    private Article article;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .build();

        article = Article.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .user(user)
                .build();
    }

    @Test
    public void testCreateArticle() {
        when(customUserDetails.getUser()).thenReturn(user);
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        ArticleRequestDTO.CreateArticleDTO createArticleDTO = new ArticleRequestDTO.CreateArticleDTO("Test Title", "Test Content");
        ArticleResponseDTO.ArticleDTO result = articleService.createArticle(createArticleDTO, customUserDetails);

        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getContent()).isEqualTo("Test Content");
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    public void 게시글_수정_성공시_업데이트된_게시글을_반환한다() {
        ArticleRequestDTO.UpdateArticleDTO updateArticleDTO = new ArticleRequestDTO.UpdateArticleDTO("Updated Title", "Updated Content");

        when(articleRepository.findById(anyLong())).thenReturn(Optional.of(article));
        when(customUserDetails.getUser()).thenReturn(user);

        ArticleResponseDTO.ArticleDTO result = articleService.updateArticle(1L, updateArticleDTO, customUserDetails);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getContent()).isEqualTo("Updated Content");
    }

//    @Test
//    public void 게시글_수정_본인이_아닐경우_예외를_발생시킨다() {
//        ArticleRequestDTO.UpdateArticleDTO updateArticleDTO = new ArticleRequestDTO.UpdateArticleDTO("Updated Title", "Updated Content");
//
//        User anotherUser = User.builder().id(2L).build(); // 다른 유저
//        when(articleRepository.findById(anyLong())).thenReturn(Optional.of(article));
//        when(customUserDetails.getUser()).thenReturn(anotherUser);
//
//        assertThatThrownBy(() -> articleService.updateArticle(1L, updateArticleDTO, customUserDetails))
//                .isInstanceOf(new ArticleException(ErrorStatus.NOT_VALID_USER).getClass())
//                .hasMessageContaining("자신이 작성한 게시글, 댓글만 수정 및 삭제 할 수 있습니다.");
//    }

    @Test
    public void 게시글_삭제_성공시_정상적으로_삭제된다() {
        when(articleRepository.findById(anyLong())).thenReturn(Optional.of(article));
        when(customUserDetails.getUser()).thenReturn(user);

        articleService.deleteArticle(1L, customUserDetails);

        verify(articleRepository, times(1)).delete(any(Article.class));
    }

//    @Test
//    public void 게시글_삭제_본인이_아닐경우_예외를_발생시킨다() {
//        User anotherUser = User.builder().id(2L).build(); // 다른 유저
//        when(articleRepository.findById(anyLong())).thenReturn(Optional.of(article));
//        when(customUserDetails.getUser()).thenReturn(anotherUser);
//
//        assertThatThrownBy(() -> articleService.deleteArticle(1L, customUserDetails))
//                .isInstanceOf(ArticleException.class)
//                .hasMessageContaining("자신이 작성한 게시글");
//    }

}
