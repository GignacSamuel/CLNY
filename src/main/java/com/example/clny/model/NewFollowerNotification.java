package com.example.clny.model;

import jakarta.persistence.Entity;

@Entity
public class NewFollowerNotification extends Notification {

    private Long followerId;

    public NewFollowerNotification() {
    }

    public NewFollowerNotification(User user, Long followerId) {
        super(user);
        this.followerId = followerId;
    }

}
