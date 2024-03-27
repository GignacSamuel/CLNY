package com.example.clny.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.example.clny.exception.custom.AlreadyFollowingException;
import com.example.clny.exception.custom.NotFollowingException;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clny.exception.GlobalExceptionHandler;
import com.example.clny.filter.JwtAuthenticationFilter;
import com.example.clny.service.JwtService;
import com.example.clny.service.UserFollowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserFollowController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UserFollowControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserFollowService userFollowService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserFollowController userFollowController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userFollowController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllFollowedIdsTest_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;
        List<Long> follows = Arrays.asList(2L,3L);

        when(userFollowService.getAllFollowedIds(userId)).thenReturn(follows);

        String expected = objectMapper.writeValueAsString(follows);

        // Act and Assert
        mockMvc.perform(get("/userfollow/getFollowedIds/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void getAllFollowerIdsTest_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;
        List<Long> followers = Arrays.asList(2L,3L);

        when(userFollowService.getAllFollowerIds(userId)).thenReturn(followers);

        String expected = objectMapper.writeValueAsString(followers);

        // Act and Assert
        mockMvc.perform(get("/userfollow/getFollowerIds/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void followUserTest_HappyPath() throws Exception {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;

        List<Long> follows = List.of(2L);

        when(userFollowService.followUser(followerId, followedId)).thenReturn(follows);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("followerId", String.valueOf(followerId));
        requestBody.put("followedId", String.valueOf(followedId));

        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
        String expected = objectMapper.writeValueAsString(follows);

        // Act and Assert
        mockMvc.perform(post("/userfollow/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void followUserTest_AlreadyFollowing() throws Exception {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;

        when(userFollowService.followUser(followerId, followedId)).thenThrow(AlreadyFollowingException.class);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("followerId", String.valueOf(followerId));
        requestBody.put("followedId", String.valueOf(followedId));

        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        // Act and Assert
        mockMvc.perform(post("/userfollow/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unfollowUserTest_HappyPath() throws Exception {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;

        List<Long> follows = List.of();

        when(userFollowService.unfollowUser(followerId, followedId)).thenReturn(follows);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("followerId", String.valueOf(followerId));
        requestBody.put("followedId", String.valueOf(followedId));

        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
        String expected = objectMapper.writeValueAsString(follows);

        // Act and Assert
        mockMvc.perform(post("/userfollow/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void unfollowUserTest_NotFollowing() throws Exception {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;

        when(userFollowService.unfollowUser(followerId, followedId)).thenThrow(NotFollowingException.class);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("followerId", String.valueOf(followerId));
        requestBody.put("followedId", String.valueOf(followedId));

        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        // Act and Assert
        mockMvc.perform(post("/userfollow/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest());
    }

}
