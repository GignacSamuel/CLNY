package com.example.clny.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDTO {

    private Long id;

    @NotBlank
    private String content;

    private Date commentDate;

    @NotNull
    private UserDTO author;

    @NotNull
    private PostDTO post;

    private CommentDTO parentComment;

    public CommentDTO() {
    }

    public CommentDTO(String content, UserDTO author, PostDTO post) {
        this.content = content;
        this.author = author;
        this.post = post;
    }

}
