package com.example.clny.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConversationDTO {

    private Long id;

    private List<UserDTO> participants;

    public ConversationDTO() {
    }

    public ConversationDTO(List<UserDTO> participants) {
        this.participants = participants;
    }

}
