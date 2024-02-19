package com.example.clny.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    private final Date commentDate = new Date();

    @ManyToOne
    private User author;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Comment parentComment;

    public Comment() {
    }

    public Comment(String content, User author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
    }

}
