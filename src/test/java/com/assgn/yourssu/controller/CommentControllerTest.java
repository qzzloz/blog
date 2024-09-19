package com.assgn.yourssu.controller;

import com.assgn.yourssu.dto.CommentRequestDTO;
import com.assgn.yourssu.dto.CommentResponseDTO;
import com.assgn.yourssu.security.CustomUserDetails;
import com.assgn.yourssu.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class CommentControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CustomUserDetails customUserDetails;


    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
//        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void 새_댓글_작성_성공시_댓글_정보를_반환한다() throws Exception {
        CommentRequestDTO.CreateCommentDTO request = new CommentRequestDTO.CreateCommentDTO("Test comment");

        CommentResponseDTO response = CommentResponseDTO.builder()
                .commentId(1L)
                .email("test@example.com")
                .content("This is a comment")
                .build();

        when(commentService.createComment(anyLong(), any(CommentRequestDTO.CreateCommentDTO.class), any(CustomUserDetails.class)))
                .thenReturn(response);

        mockMvc.perform(post("/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.content").value("This is a comment"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    public void 댓글_작성_실패_내용이_누락된_경우_에러_발생() throws Exception {
        CommentRequestDTO.CreateCommentDTO request = new CommentRequestDTO.CreateCommentDTO(" ");

        mockMvc.perform(post("/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    public void 댓글_수정_성공시_수정된_댓글_정보를_반환한다() throws Exception {
        CommentRequestDTO.UpdateCommentDTO request = new CommentRequestDTO.UpdateCommentDTO("Updated comment");

        CommentResponseDTO response = CommentResponseDTO.builder()
                .commentId(1L)
                .email("test@example.com")
                .content("Updated comment")
                .build();

        when(commentService.updateComment(anyLong(), any(CommentRequestDTO.UpdateCommentDTO.class), any(CustomUserDetails.class)))
                .thenReturn(response);

        mockMvc.perform(put("/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.content").value("Updated comment"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    public void 댓글_삭제_성공시_성공_메시지를_반환한다() throws Exception {
        doNothing().when(commentService).deleteComment(anyLong(), any(CustomUserDetails.class));

        mockMvc.perform(delete("/comment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));

        verify(commentService, times(1)).deleteComment(anyLong(), any(CustomUserDetails.class));
    }


}
