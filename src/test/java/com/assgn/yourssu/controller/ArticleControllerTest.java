package com.assgn.yourssu.controller;

import com.assgn.yourssu.domain.common.ApiResponse;
import com.assgn.yourssu.domain.common.BaseErrorCode;
import com.assgn.yourssu.dto.ArticleRequestDTO;
import com.assgn.yourssu.dto.ArticleResponseDTO;
import com.assgn.yourssu.dto.ErrorReasonDTO;
import com.assgn.yourssu.exception.ArticleException;
import com.assgn.yourssu.exception.ExceptionAdvice;
import com.assgn.yourssu.exception.GeneralException;
import com.assgn.yourssu.security.CustomUserDetails;
import com.assgn.yourssu.security.CustomUserDetailsService;
import com.assgn.yourssu.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.assgn.yourssu.domain.common.ErrorStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Nested
//@ContextConfiguration(classes = ArticleController.class)
//@Import({ArticleController.class, ExceptionAdvice.class, CustomUserDetailsService.class, ArticleException.class})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(ArticleController.class)  // 컨트롤러 테스트에 필요한 애너테이션
 class ArticleControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();;

    @InjectMocks
    private ArticleController articleController;

    @MockBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
//    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void 게시글_생성_성공_시_Article을_반환한다() throws Exception {
        // Given
        ArticleRequestDTO.CreateArticleDTO request = new ArticleRequestDTO.CreateArticleDTO("Title", "Content");
        ArticleResponseDTO.ArticleDTO response = ArticleResponseDTO.ArticleDTO.builder()
                .articleId(1L)
                .title("Title")
                .content("Content")
                .email("user@example.com")
                .build();

        when(articleService.createArticle(any(ArticleRequestDTO.CreateArticleDTO.class), any(CustomUserDetails.class)))
                .thenReturn(response);

        // When && Then
        mockMvc.perform(post("/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(() -> "user@example.com")) // Mock principal correctly
                .andDo(MockMvcResultHandlers.print())  // 응답 본문 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleId").value(1))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.content").value("Content"))
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void 게시글_생성_실패_제목_누락시_에러를_반환한다() throws Exception {
        ArticleRequestDTO.CreateArticleDTO request = new ArticleRequestDTO.CreateArticleDTO("", "Content");

        willThrow(new ArticleException(ErrorStatus._BAD_REQUEST)).given(articleService)
                .createArticle(any(ArticleRequestDTO.CreateArticleDTO.class), any(CustomUserDetails.class));

//        verify(articleService).createArticle(
//                any(ArticleRequestDTO.CreateArticleDTO.class), any(CustomUserDetails.class));

        verify(articleService, times(1))
                .createArticle(any(ArticleRequestDTO.CreateArticleDTO.class), any(CustomUserDetails.class));


        mockMvc.perform(post("/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(() -> "test@example.com"))
                .andDo(MockMvcResultHandlers.print())  // 응답 본문 출력
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청"))
                .andExpect(jsonPath("$.content.title").value("공백일 수 없습니다"));
    }



//    @Test
//    @WithMockUser(username = "test@example.com", roles = {"USER"})
//    public void 게시글_생성_실패_내용_누락시_에러를_반환한다() throws Exception {
//        ArticleRequestDTO.CreateArticleDTO request = new ArticleRequestDTO.CreateArticleDTO("Valid Title", "");
//
//        mockMvc.perform(post("/article")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.statusCode").value("BAD_REQUEST"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청"));
//    }


    @Test
    @WithMockUser(username = "user@example.com")
    public void 게시글_수정_성공시_Article을_반환한다() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleRequestDTO.UpdateArticleDTO request = new ArticleRequestDTO.UpdateArticleDTO("Updated Title", "Updated Content");
        ArticleResponseDTO.ArticleDTO response = ArticleResponseDTO.ArticleDTO.builder()
                .articleId(articleId)
                .title("Updated Title")
                .content("Updated Content")
                .email("user@example.com")
                .build();

        when(articleService.updateArticle(eq(articleId), any(ArticleRequestDTO.UpdateArticleDTO.class), any(CustomUserDetails.class)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(put("/article/{articleId}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(() -> "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleId").value(articleId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"))
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

//    @Test
//    @WithMockUser(username = "test@example.com", roles = {"USER"})
//    public void 게시글_수정_실패_내용_누락시_에러를_반환한다() throws Exception {
//        ArticleRequestDTO.UpdateArticleDTO request = new ArticleRequestDTO.UpdateArticleDTO("Updated Title", "");
//
//        mockMvc.perform(put("/article/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.statusCode").value("COMMON400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청"))
//                .andExpect(jsonPath("$.content.content").value("공백일 수 없습니다"));
//    }

//    @Test
//    @WithMockUser(username = "test@example.com", roles = {"USER"})
//    public void 게시글_수정_실패_존재하지_않는_게시글일_경우_에러를_반환한다() throws Exception {
//        ArticleRequestDTO.UpdateArticleDTO request = new ArticleRequestDTO.UpdateArticleDTO("Updated Title", "Updated Content");
//
//        when(articleService.updateArticle(anyLong(), any(ArticleRequestDTO.UpdateArticleDTO.class), any(CustomUserDetails.class)))
//                .thenThrow(new ArticleException(ErrorStatus.ARTICLE_NOT_EXITS));
//
//        mockMvc.perform(put("/article/100")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.statusCode").value("BAD_REQUEST"))
//                .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다."));
//    }


    @Test
    @WithMockUser(username = "test@example.com")
    public void 게시글_삭제_성공시_정상적인_응답을_반환한다() throws Exception {
        mockMvc.perform(delete("/article/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));

        verify(articleService, times(1)).deleteArticle(anyLong(), any(CustomUserDetails.class));
    }


//    @Test
//    @WithMockUser(username = "test@example.com", roles = {"USER"})
//    public void 게시글_삭제_실패_존재하지_않는_게시글일_경우_에러를_발생시킨다() throws Exception {
//        doThrow(new ArticleException(ErrorStatus.ARTICLE_NOT_EXITS))
//                .when(articleService).deleteArticle(anyLong(), any(CustomUserDetails.class));
//
//        mockMvc.perform(delete("/article/100"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.statusCode").value("BAD_REQUEST"))
//                .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다."));
//    }

}
