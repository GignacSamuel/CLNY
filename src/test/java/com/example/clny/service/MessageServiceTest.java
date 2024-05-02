package com.example.clny.service;

import com.example.clny.dto.UserDTO;
import com.example.clny.exception.custom.EmptyMessageException;
import com.example.clny.exception.custom.InvalidConversationParticipantsException;
import com.example.clny.exception.custom.NoMessageAuthorException;
import com.example.clny.exception.custom.NoMessageConversationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.clny.dto.ConversationDTO;
import com.example.clny.dto.MessageDTO;
import com.example.clny.mapper.ConversationMapper;
import com.example.clny.mapper.MessageMapper;
import com.example.clny.model.Conversation;
import com.example.clny.model.Message;
import com.example.clny.repository.ConversationRepository;
import com.example.clny.repository.MessageRepository;
import com.example.clny.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ConversationMapper conversationMapper;

    @Mock
    private MessageMapper messageMapper;

    @Test
    void getConversationsTest_HappyPath() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(conversationRepository.findAllConversationsByUserId(userId)).thenReturn(List.of(new Conversation()));
        when(conversationMapper.conversationToConversationDTO(any())).thenReturn(new ConversationDTO());

        // Act
        List<ConversationDTO> result = messageService.getConversations(userId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getConversationsTest_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.getConversations(userId);
        }, "Expected getConversations() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void getMessagesTest_HappyPath() {
        // Arrange
        Long conversationId = 1L;

        when(conversationRepository.existsById(conversationId)).thenReturn(true);
        when(messageRepository.findAllByConversationId(conversationId)).thenReturn(List.of(new Message()));
        when(messageMapper.messageToMessageDTO(any())).thenReturn(new MessageDTO());

        // Act
        List<MessageDTO> result = messageService.getMessages(conversationId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getMessagesTest_ConversationDoesNotExist() {
        // Arrange
        Long conversationId = 1L;

        when(conversationRepository.existsById(conversationId)).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.getMessages(conversationId);
        }, "Expected getMessages() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void createConversationTest_HappyPath() throws Exception {
        // Arrange
        List<UserDTO> participants = List.of(new UserDTO(), new UserDTO());
        ConversationDTO conversationDTO = new ConversationDTO(participants);
        Conversation conversation = new Conversation();

        when(conversationMapper.conversationDTOToConversation(any())).thenReturn(conversation);
        when(conversationRepository.save(any())).thenReturn(conversation);
        when(conversationMapper.conversationToConversationDTO(any())).thenReturn(conversationDTO);

        // Act
        ConversationDTO result = messageService.createConversation(conversationDTO);

        // Assert
        assertNotNull(result);
        assertEquals(participants, result.getParticipants());
    }

    @Test
    void createConversationTest_NullParam() {
        // Arrange
        ConversationDTO conversationDTO = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.createConversation(conversationDTO);
        }, "Expected createConversation() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void createConversationTest_InvalidConversationParticipants() {
        // Arrange
        ConversationDTO conversationDTOWithNoParticipants = new ConversationDTO(new ArrayList<>());
        ConversationDTO conversationDTOWithNullParticipants = new ConversationDTO(null);

        List<UserDTO> oneParticipant = List.of(new UserDTO());
        ConversationDTO conversationDTOWithOneParticipant = new ConversationDTO(oneParticipant);

        // Act and Assert
        assertThrows(InvalidConversationParticipantsException.class, () -> {
            messageService.createConversation(conversationDTOWithNoParticipants);
        }, "Expected createConversation() to throw InvalidConversationParticipantsException, but it didn't");

        assertThrows(InvalidConversationParticipantsException.class, () -> {
            messageService.createConversation(conversationDTOWithNullParticipants);
        }, "Expected createConversation() to throw InvalidConversationParticipantsException, but it didn't");

        assertThrows(InvalidConversationParticipantsException.class, () -> {
            messageService.createConversation(conversationDTOWithOneParticipant);
        }, "Expected createConversation() to throw InvalidConversationParticipantsException, but it didn't");
    }

    @Test
    void sendMessageTest_HappyPath() throws Exception {
        // Arrange
        UserDTO author = new UserDTO();
        ConversationDTO conversation = new ConversationDTO();

        MessageDTO messageDTO = new MessageDTO("Hello, World!", author, conversation);
        Message message = new Message();

        when(messageMapper.messageDTOToMessage(any(MessageDTO.class))).thenReturn(message);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(conversationRepository.existsById(any())).thenReturn(true);

        // Act
        List<MessageDTO> result = messageService.sendMessage(messageDTO);

        // Assert
        assertNotNull(result);
    }

    @Test
    void sendMessageTest_NullParam() {
        // Arrange
        MessageDTO messageDTO = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage(messageDTO);
        }, "Expected sendMessage() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void sendMessageTest_EmptyMessage() {
        // Arrange
        UserDTO author = new UserDTO();
        ConversationDTO conversation = new ConversationDTO();

        MessageDTO messageDTO = new MessageDTO("   ", author, conversation);

        // Act and Assert
        assertThrows(EmptyMessageException.class, () -> {
            messageService.sendMessage(messageDTO);
        }, "Expected sendMessage() to throw EmptyMessageException, but it didn't");
    }

    @Test
    void sendMessageTest_NoMessageAuthor() {
        // Arrange
        ConversationDTO conversation = new ConversationDTO();

        MessageDTO messageDTO = new MessageDTO("Message content", null, conversation);

        // Act and Assert
        assertThrows(NoMessageAuthorException.class, () -> {
            messageService.sendMessage(messageDTO);
        }, "Expected sendMessage() to throw NoMessageAuthorException, but it didn't");
    }

    @Test
    void sendMessageTest_NoMessageConversation() {
        // Arrange
        UserDTO author = new UserDTO();

        MessageDTO messageDTO = new MessageDTO("Message content", author, null);

        // Act and Assert
        assertThrows(NoMessageConversationException.class, () -> {
            messageService.sendMessage(messageDTO);
        }, "Expected sendMessage() to throw NoMessageConversationException, but it didn't");
    }

}
