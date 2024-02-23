package com.example.clny.mapper;

import com.example.clny.dto.MessageDTO;
import com.example.clny.model.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageDTO messageToMessageDTO(Message message);

    Message messageDTOToMessage(MessageDTO messageDTO);

}
