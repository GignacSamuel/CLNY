package com.example.clny.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MessageDTO {

    private Long id;

    @NotBlank
    private String content;

    private Date messageDate;

    @NotNull
    private UserDTO author;

    @NotNull
    private ConversationDTO conversation;

    public MessageDTO() {
    }

    public MessageDTO(String content, UserDTO author, ConversationDTO conversation) {
        this.content = content;
        this.author = author;
        this.conversation = conversation;
    }

}
