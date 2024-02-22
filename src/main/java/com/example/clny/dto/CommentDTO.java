package com.example.clny.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDTO {

    private Long id;

    private String content;

    private Date commentDate;

    private UserDTO author;

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
