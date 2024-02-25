package com.example.clny.dto;

import com.example.clny.model.ReactionType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReactionDTO {

    private Long id;

    @NotNull
    private ReactionType type;

    private Date reactionDate;

    @NotNull
    private UserDTO author;

    @NotNull
    private PostDTO post;

    public ReactionDTO() {
    }

    public ReactionDTO(ReactionType type, UserDTO author, PostDTO post) {
        this.type = type;
        this.author = author;
        this.post = post;
    }

}
