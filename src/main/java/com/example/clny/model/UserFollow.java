package com.example.clny.model;

import jakarta.persistence.*;
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

    @ManyToOne
    private User follower;

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
