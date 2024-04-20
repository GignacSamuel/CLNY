package com.example.clny.controller;

import com.example.clny.dto.PostDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.GlobalExceptionHandler;
import com.example.clny.exception.custom.EmptyPostException;
import com.example.clny.exception.custom.NoPostAuthorException;
import com.example.clny.exception.custom.PostTooLongException;
import com.example.clny.filter.JwtAuthenticationFilter;
import com.example.clny.service.JwtService;
import com.example.clny.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PostController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class PostControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostController postController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getPostsFromUserTest_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;

        List<PostDTO> postDTOs = List.of(new PostDTO("Test content", new ArrayList<>(), new UserDTO()));

        when(postService.getPostsFromUser(userId)).thenReturn(postDTOs);

        // Act and Assert
        mockMvc.perform(get("/post/getPosts/{userId}", userId))
                .andExpect(status().isOk());
    }

    @Test
    void deletePostTest_HappyPath() throws Exception {
        // Arrange
        Long postId = 1L;
        Long userId = 1L;

        UserDTO authorDTO = new UserDTO();
        authorDTO.setId(userId);

        List<PostDTO> postDTOs = List.of(new PostDTO("Post deleted content", new ArrayList<>(), authorDTO));

        when(postService.deletePost(postId)).thenReturn(postDTOs);

        // Act and Assert
        mockMvc.perform(delete("/post/deletePost/{postId}", postId))
                .andExpect(status().isOk());
    }

    @Test
    void createPostTest_HappyPath() throws Exception {
        // Arrange
        String content = "This is a test post";
        Long userId = 1L;

        UserDTO authorDTO = new UserDTO();
        authorDTO.setId(userId);

        PostDTO postDTO = new PostDTO(content, new ArrayList<>(), authorDTO);
        List<PostDTO> postDTOs = List.of(postDTO);

        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "test data".getBytes());
        MockMultipartFile mockPostDTO = new MockMultipartFile("postDTO", "", "application/json", objectMapper.writeValueAsBytes(postDTO));

        when(postService.createPost(any(PostDTO.class), anyList())).thenReturn(postDTOs);

        // Act and Assert
        mockMvc.perform(multipart("/post/createPost")
                .file(mockFile)
                .file(mockPostDTO))
                .andExpect(status().isOk());
    }

    @Test
    void createPostTest_EmptyPost() throws Exception {
        // Arrange
        PostDTO emptyPostDTO = new PostDTO("", new ArrayList<>(), new UserDTO());

        MockMultipartFile mockPostDTO = new MockMultipartFile("postDTO", "", "application/json", objectMapper.writeValueAsBytes(emptyPostDTO));
        MockMultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain", "Some content".getBytes());

        when(postService.createPost(any(PostDTO.class), anyList())).thenThrow(EmptyPostException.class);

        // Act and Assert
        mockMvc.perform(multipart("/post/createPost")
                .file(mockFile)
                .file(mockPostDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPostTest_PostTooLong() throws Exception {
        // Arrange
        PostDTO longPostDTO = new PostDTO("a".repeat(251), new ArrayList<>(), new UserDTO());

        MockMultipartFile mockPostDTO = new MockMultipartFile("postDTO", "", "application/json", objectMapper.writeValueAsBytes(longPostDTO));
        MockMultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain", "Some content".getBytes());

        when(postService.createPost(any(PostDTO.class), anyList())).thenThrow(PostTooLongException.class);

        // Act and Assert
        mockMvc.perform(multipart("/post/createPost")
                .file(mockFile)
                .file(mockPostDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPostTest_NoPostAuthor() throws Exception {
        // Arrange
        PostDTO noAuthorPostDTO = new PostDTO("Valid content", new ArrayList<>(), null);

        MockMultipartFile mockPostDTO = new MockMultipartFile("postDTO", "", "application/json", objectMapper.writeValueAsBytes(noAuthorPostDTO));
        MockMultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain", "Some content".getBytes());

        when(postService.createPost(any(PostDTO.class), anyList())).thenThrow(NoPostAuthorException.class);

        // Act and Assert
        mockMvc.perform(multipart("/post/createPost")
                .file(mockFile)
                .file(mockPostDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFeedPostsTest_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;

        List<PostDTO> postDTOs = List.of(new PostDTO("content",new ArrayList<>(), new UserDTO()));

        when(postService.getFeedPosts(userId)).thenReturn(postDTOs);

        // Act and Assert
        mockMvc.perform(get("/post/getFeed/{userId}", userId))
                .andExpect(status().isOk());
    }

}
