package com.example.clny.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewFollowerNotificationDTO extends NotificationDTO {

    private Long followerId;

    public NewFollowerNotificationDTO() {
    }

    public NewFollowerNotificationDTO(UserDTO user, Long followerId) {
        super(user);
        this.followerId = followerId;
    }

}
