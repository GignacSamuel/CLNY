package com.example.clny.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class UserFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne
    private User follower;

    @NotNull
    @ManyToOne
    private User followed;

    private final Date followDate = new Date();

    public UserFollow() {
    }

    public UserFollow(User follower, User followed) {
        this.follower = follower;
        this.followed = followed;
    }

}
