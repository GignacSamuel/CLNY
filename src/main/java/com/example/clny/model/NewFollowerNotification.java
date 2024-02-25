package com.example.clny.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NewFollowerNotification extends Notification {

    @NotNull
    private Long followerId;

    public NewFollowerNotification() {
    }

    public NewFollowerNotification(User user, Long followerId) {
        super(user);
        this.followerId = followerId;
    }

}
