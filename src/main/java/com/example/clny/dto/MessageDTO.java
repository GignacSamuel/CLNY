package com.example.clny.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MessageDTO {

    private Long id;

    private String content;

    private Date messageDate;

    private UserDTO author;

    private ConversationDTO conversation;

    public MessageDTO() {
    }

    public MessageDTO(String content, UserDTO author, ConversationDTO conversation) {
        this.content = content;
        this.author = author;
        this.conversation = conversation;
    }

}
