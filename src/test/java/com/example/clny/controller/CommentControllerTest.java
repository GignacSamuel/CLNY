package com.example.clny.controller;

import com.example.clny.dto.CommentDTO;
import com.example.clny.dto.PostDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.GlobalExceptionHandler;
import com.example.clny.exception.custom.CommentTooLongException;
import com.example.clny.exception.custom.EmptyCommentException;
import com.example.clny.exception.custom.NoCommentAuthorException;
import com.example.clny.exception.custom.NoCommentPostException;
import com.example.clny.filter.JwtAuthenticationFilter;
import com.example.clny.service.CommentService;
import com.example.clny.service.JwtService;
import com.example.clny.util.CommentNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CommentController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class CommentControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentController commentController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getCommentsFromPostTest_HappyPath() throws Exception {
        // Arrange
        Long postId = 1L;
        List<CommentNode> commentNodes = List.of(new CommentNode(new CommentDTO("Test comment", new UserDTO(), new PostDTO())));

        when(commentService.getCommentsFromPost(postId)).thenReturn(commentNodes);

        // Act and Assert
        mockMvc.perform(get("/comment/getComments/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createCommentTest_HappyPath() throws Exception {
        // Arrange
        CommentDTO commentDTO = new CommentDTO("Test comment", new UserDTO(), new PostDTO());
        CommentNode commentNode = new CommentNode(commentDTO);
        List<CommentNode> commentNodes = List.of(commentNode);

        when(commentService.createComment(any(CommentDTO.class))).thenReturn(commentNodes);

        // Act and Assert
        mockMvc.perform(post("/comment/createComment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void createCommentTest_EmptyComment() throws Exception {
        // Arrange
        CommentDTO commentDTO = new CommentDTO("", new UserDTO(), new PostDTO());
        when(commentService.createComment(any(CommentDTO.class))).thenThrow(new EmptyCommentException());

        // Act and Assert
        mockMvc.perform(post("/comment/createComment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCommentTest_CommentTooLong() throws Exception {
        // Arrange
        CommentDTO commentDTO = new CommentDTO("a".repeat(251), new UserDTO(), new PostDTO());
        when(commentService.createComment(any(CommentDTO.class))).thenThrow(new CommentTooLongException());

        // Act and Assert
        mockMvc.perform(post("/comment/createComment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCommentTest_NoCommentAuthor() throws Exception {
        // Arrange
        CommentDTO commentDTO = new CommentDTO("Valid comment", null, new PostDTO());
        when(commentService.createComment(any(CommentDTO.class))).thenThrow(new NoCommentAuthorException());

        // Act and Assert
        mockMvc.perform(post("/comment/createComment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCommentTest_NoCommentPost() throws Exception {
        // Arrange
        CommentDTO commentDTO = new CommentDTO("Valid comment", new UserDTO(), null);
        when(commentService.createComment(any(CommentDTO.class))).thenThrow(new NoCommentPostException());

        // Act and Assert
        mockMvc.perform(post("/comment/createComment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isBadRequest());
    }

}
