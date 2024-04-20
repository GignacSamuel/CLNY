package com.example.clny.controller;

import com.example.clny.dto.PostDTO;
import com.example.clny.dto.ReactionDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.GlobalExceptionHandler;
import com.example.clny.exception.custom.NoReactionAuthorException;
import com.example.clny.exception.custom.NoReactionPostException;
import com.example.clny.exception.custom.NoReactionTypeException;
import com.example.clny.filter.JwtAuthenticationFilter;
import com.example.clny.model.ReactionType;
import com.example.clny.service.JwtService;
import com.example.clny.service.ReactionService;
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
@WebMvcTest(controllers = ReactionController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ReactionControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ReactionService reactionService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReactionController reactionController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(reactionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getPostReactionsTest_HappyPath() throws Exception {
        // Arrange
        Long postId = 1L;

        List<ReactionDTO> reactionDTOs = List.of(new ReactionDTO());

        when(reactionService.getPostReactions(postId)).thenReturn(reactionDTOs);

        // Act and Assert
        mockMvc.perform(get("/reaction/getReactions/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addReactionTest_HappyPath() throws Exception {
        // Arrange
        ReactionDTO reactionDTO = new ReactionDTO();
        List<ReactionDTO> expectedResponse = List.of(reactionDTO);

        when(reactionService.addReaction(reactionDTO)).thenReturn(expectedResponse);

        // Act and Assert
        mockMvc.perform(post("/reaction/addReaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reactionDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void addReactionTest_NoReactionType() throws Exception {
        // Arrange
        ReactionDTO reactionDTO = new ReactionDTO(null, new UserDTO(), new PostDTO());

        when(reactionService.addReaction(any(ReactionDTO.class))).thenThrow(new NoReactionTypeException());

        // Act and Assert
        mockMvc.perform(post("/reaction/addReaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reactionDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addReactionTest_NoReactionAuthor() throws Exception {
        // Arrange
        ReactionDTO reactionDTO = new ReactionDTO(ReactionType.LIKE, null, new PostDTO());

        when(reactionService.addReaction(any(ReactionDTO.class))).thenThrow(new NoReactionAuthorException());

        // Act and Assert
        mockMvc.perform(post("/reaction/addReaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reactionDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addReactionTest_NoReactionPost() throws Exception {
        // Arrange
        ReactionDTO reactionDTO = new ReactionDTO(ReactionType.LIKE, new UserDTO(), null);

        when(reactionService.addReaction(any(ReactionDTO.class))).thenThrow(new NoReactionPostException());

        // Act and Assert
        mockMvc.perform(post("/reaction/addReaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reactionDTO)))
                .andExpect(status().isBadRequest());
    }

}
