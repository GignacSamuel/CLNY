package com.example.clny.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewMessageNotificationDTO extends NotificationDTO {

    @NotNull
    private Long messageId;

    public NewMessageNotificationDTO() {
    }

    public NewMessageNotificationDTO(UserDTO user, Long messageId) {
        super(user);
        this.messageId = messageId;
    }

}
