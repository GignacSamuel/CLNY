package com.example.clny.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConversationDTO {

    private Long id;

    @NotNull
    @NotEmpty
    private List<UserDTO> participants;

    public ConversationDTO() {
    }

    public ConversationDTO(List<UserDTO> participants) {
        this.participants = participants;
    }

}
