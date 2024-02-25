package com.example.clny.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCommentNotificationDTO extends NotificationDTO {

    @NotNull
    private Long commentId;

    public NewCommentNotificationDTO() {
    }

    public NewCommentNotificationDTO(UserDTO user, Long commentId) {
        super(user);
        this.commentId = commentId;
    }

}
