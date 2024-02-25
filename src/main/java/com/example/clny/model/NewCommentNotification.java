package com.example.clny.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NewCommentNotification extends Notification {

    @NotNull
    private Long commentId;

    public NewCommentNotification() {
    }

    public NewCommentNotification(User user, Long commentId) {
        super(user);
        this.commentId = commentId;
    }

}
