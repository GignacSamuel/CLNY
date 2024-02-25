package com.example.clny.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NewMessageNotification extends Notification {

    @NotNull
    private Long messageId;

    public NewMessageNotification() {
    }

    public NewMessageNotification(User user, Long messageId) {
        super(user);
        this.messageId = messageId;
    }

}
