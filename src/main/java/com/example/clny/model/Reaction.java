package com.example.clny.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    private final Date reactionDate = new Date();

    @ManyToOne
    private User author;

    @ManyToOne
    private Post post;

    public Reaction() {
    }

    public Reaction(ReactionType type, User author, Post post) {
        this.type = type;
        this.author = author;
        this.post = post;
    }

}
