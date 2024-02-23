package com.example.clny.mapper;

import com.example.clny.dto.NewCommentNotificationDTO;
import com.example.clny.model.NewCommentNotification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewCommentNotificationMapper {

    NewCommentNotificationDTO newCommentNotificationToNewCommentNotificationDTO(NewCommentNotification newCommentNotification);

    NewCommentNotification newCommentNotificationDTOToNewCommentNotification(NewCommentNotificationDTO newCommentNotificationDTO);

}
