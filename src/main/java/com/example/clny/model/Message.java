package com.example.clny.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    private final Date messageDate = new Date();

    @ManyToOne
    private User author;

    @ManyToOne
    private Conversation conversation;

    public Message() {
    }

    public Message(String content, User author, Conversation conversation) {
        this.content = content;
        this.author = author;
        this.conversation = conversation;
    }

}
