package com.example.clny.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCommentNotificationDTO extends NotificationDTO {

    private Long commentId;

    public NewCommentNotificationDTO() {
    }

    public NewCommentNotificationDTO(UserDTO user, Long commentId) {
        super(user);
        this.commentId = commentId;
    }

}
