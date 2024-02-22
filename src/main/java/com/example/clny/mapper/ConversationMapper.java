package com.example.clny.mapper;

import com.example.clny.dto.ConversationDTO;
import com.example.clny.model.Conversation;
import org.mapstruct.Mapper;

@Mapper
public interface ConversationMapper {

    ConversationDTO conversationToConversationDTO(Conversation conversation);

    Conversation conversationDTOToConversation(ConversationDTO conversationDTO);

}
