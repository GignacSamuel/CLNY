package com.example.clny.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewFollowerNotificationDTO extends NotificationDTO {

    @NotNull
    private Long followerId;

    public NewFollowerNotificationDTO() {
    }

    public NewFollowerNotificationDTO(UserDTO user, Long followerId) {
        super(user);
        this.followerId = followerId;
    }

}
