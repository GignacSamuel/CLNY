package com.example.clny.mapper;

import com.example.clny.dto.NewMessageNotificationDTO;
import com.example.clny.model.NewMessageNotification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewMessageNotificationMapper {

    NewMessageNotificationDTO newMessageNotificationToNewMessageNotificationDTO(NewMessageNotification newMessageNotification);

    NewMessageNotification newMessageNotificationDTOToNewMessageNotification(NewMessageNotificationDTO newMessageNotificationDTO);

}
