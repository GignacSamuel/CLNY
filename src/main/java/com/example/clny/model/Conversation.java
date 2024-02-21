package com.example.clny.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
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
