package com.example.clny.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String content;

    private final Date messageDate = new Date();

    @NotNull
    @ManyToOne
    private User author;

    @NotNull
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
