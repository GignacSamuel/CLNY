package com.example.clny.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReactionType type;

    private final Date reactionDate = new Date();

    @NotNull
    @ManyToOne
    private User author;

    @NotNull
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
