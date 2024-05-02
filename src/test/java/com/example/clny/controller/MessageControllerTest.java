package com.example.clny.controller;

import com.example.clny.dto.ConversationDTO;
import com.example.clny.dto.MessageDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.GlobalExceptionHandler;
import com.example.clny.exception.custom.EmptyMessageException;
import com.example.clny.exception.custom.InvalidConversationParticipantsException;
import com.example.clny.exception.custom.NoMessageAuthorException;
import com.example.clny.exception.custom.NoMessageConversationException;
import com.example.clny.filter.JwtAuthenticationFilter;
import com.example.clny.service.JwtService;
import com.example.clny.service.MessageService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MessageController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class MessageControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageController messageController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(messageController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getConversationsTest_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;
        List<ConversationDTO> conversations = List.of(new ConversationDTO());

        when(messageService.getConversations(userId)).thenReturn(conversations);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/message/getConversations/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(conversations)));
    }

    @Test
    void getMessagesTest_HappyPath() throws Exception {
        // Arrange
        Long conversationId = 1L;
        List<MessageDTO> messages = List.of(new MessageDTO());

        when(messageService.getMessages(conversationId)).thenReturn(messages);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/message/getMessages/{conversationId}", conversationId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(messages)));
    }

    @Test
    void createConversationTest_HappyPath() throws Exception {
        // Arrange
        ConversationDTO conversationDTO = new ConversationDTO(List.of(new UserDTO(), new UserDTO()));

        when(messageService.createConversation(any(ConversationDTO.class))).thenReturn(conversationDTO);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/message/createConversation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conversationDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(conversationDTO)));
    }

    @Test
    void createConversationTest_InvalidConversationParticipants() throws Exception {
        // Arrange
        ConversationDTO conversationDTO = new ConversationDTO(List.of());

        when(messageService.createConversation(any(ConversationDTO.class))).thenThrow(new InvalidConversationParticipantsException());

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/message/createConversation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conversationDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void sendMessageTest_HappyPath() throws Exception {
        // Arrange
        MessageDTO messageDTO = new MessageDTO("Hello", new UserDTO(), new ConversationDTO());
        List<MessageDTO> messages = List.of(messageDTO);

        when(messageService.sendMessage(any(MessageDTO.class))).thenReturn(messages);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/message/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(messages)));
    }

    @Test
    void sendMessageTest_EmptyMessage() throws Exception {
        // Arrange
        MessageDTO messageDTO = new MessageDTO("   ", new UserDTO(), new ConversationDTO());

        when(messageService.sendMessage(any(MessageDTO.class))).thenThrow(new EmptyMessageException());

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/message/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void sendMessageTest_NoMessageAuthor() throws Exception {
        // Arrange
        MessageDTO messageDTO = new MessageDTO("Hello", null, new ConversationDTO());

        when(messageService.sendMessage(any(MessageDTO.class))).thenThrow(new NoMessageAuthorException());

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/message/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void sendMessageTest_NoMessageConversation() throws Exception {
        // Arrange
        MessageDTO messageDTO = new MessageDTO("Hello", new UserDTO(), null);

        when(messageService.sendMessage(any(MessageDTO.class))).thenThrow(new NoMessageConversationException());

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/message/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
