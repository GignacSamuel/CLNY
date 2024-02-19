package com.example.clny.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    private List<User> participants;

    public Conversation() {
    }

    public Conversation(List<User> participants) {
        this.participants = participants;
    }

}
