package com.example.clny.dto;

import com.example.clny.model.ReactionType;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReactionDTO {

    private Long id;

    private ReactionType type;

    private Date reactionDate;

    private UserDTO author;

    private PostDTO post;

    public ReactionDTO() {
    }

    public ReactionDTO(ReactionType type, UserDTO author, PostDTO post) {
        this.type = type;
        this.author = author;
        this.post = post;
    }

}
