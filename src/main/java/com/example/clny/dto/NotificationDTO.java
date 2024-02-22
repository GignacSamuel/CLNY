package com.example.clny.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class NotificationDTO {

    private Long id;

    private UserDTO user;

    private Date notificationDate;

    private boolean read;

    public NotificationDTO() {
    }

    public NotificationDTO(UserDTO user) {
        this.user = user;
    }

}
