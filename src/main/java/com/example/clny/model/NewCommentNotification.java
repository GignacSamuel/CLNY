package com.example.clny.model;

import jakarta.persistence.Entity;

@Entity
public class NewCommentNotification extends Notification {

    private Long commentId;

    public NewCommentNotification() {
    }

    public NewCommentNotification(User user, Long commentId) {
        super(user);
        this.commentId = commentId;
    }

}
