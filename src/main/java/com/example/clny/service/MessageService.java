package com.example.clny.service;

import com.example.clny.dto.ConversationDTO;
import com.example.clny.dto.MessageDTO;
import com.example.clny.exception.custom.EmptyMessageException;
import com.example.clny.exception.custom.InvalidConversationParticipantsException;
import com.example.clny.exception.custom.NoMessageAuthorException;
import com.example.clny.exception.custom.NoMessageConversationException;
import com.example.clny.mapper.ConversationMapper;
import com.example.clny.mapper.MessageMapper;
import com.example.clny.model.Conversation;
import com.example.clny.model.Message;
import com.example.clny.repository.ConversationRepository;
import com.example.clny.repository.MessageRepository;
import com.example.clny.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final UserRepository userRepository;

    private final ConversationRepository conversationRepository;

    private final MessageRepository messageRepository;

    private final ConversationMapper conversationMapper;

    private final MessageMapper messageMapper;

    public MessageService(UserRepository userRepository, ConversationRepository conversationRepository, MessageRepository messageRepository, ConversationMapper conversationMapper, MessageMapper messageMapper) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.conversationMapper = conversationMapper;
        this.messageMapper = messageMapper;
    }

    public List<ConversationDTO> getConversations(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("no user with id : " + userId);
        }

        return conversationRepository.findAllConversationsByUserId(userId).stream()
                .map(conversationMapper::conversationToConversationDTO)
                .collect(Collectors.toList());
    }

    public List<MessageDTO> getMessages(Long conversationId) {
        if(!conversationRepository.existsById(conversationId)) {
            throw new IllegalArgumentException("no conversation with id : " + conversationId);
        }

        return messageRepository.findAllByConversationId(conversationId).stream()
                .map(messageMapper::messageToMessageDTO)
                .collect(Collectors.toList());
    }

    public ConversationDTO createConversation(ConversationDTO conversationDTO) throws Exception {
        if(conversationDTO == null) {
            throw new IllegalArgumentException("param conversationDTO cannot be null.");
        }

        if(conversationDTO.getParticipants() == null || conversationDTO.getParticipants().size() <= 1) {
            throw new InvalidConversationParticipantsException();
        }

        Conversation conversation = conversationMapper.conversationDTOToConversation(conversationDTO);

        return conversationMapper.conversationToConversationDTO(conversationRepository.save(conversation));
    }

    public List<MessageDTO> sendMessage(MessageDTO messageDTO) throws Exception {
        if(messageDTO == null) {
            throw new IllegalArgumentException("param messageDTO cannot be null.");
        }

        if(messageDTO.getContent() == null || messageDTO.getContent().trim().isEmpty()) {
            throw new EmptyMessageException();
        }

        if(messageDTO.getAuthor() == null) {
            throw new NoMessageAuthorException();
        }

        if(messageDTO.getConversation() == null) {
            throw new NoMessageConversationException();
        }

        Message message = messageMapper.messageDTOToMessage(messageDTO);
        messageRepository.save(message);

        return getMessages(messageDTO.getConversation().getId());
    }

}
