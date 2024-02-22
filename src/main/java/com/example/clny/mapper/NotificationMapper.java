package com.example.clny.mapper;

import com.example.clny.dto.NotificationDTO;
import com.example.clny.model.Notification;
import org.mapstruct.Mapper;

@Mapper
public interface NotificationMapper {

    NotificationDTO notificationToNotificationDTO(Notification notification);

    Notification notificationDTOToNotification(NotificationDTO notificationDTO);

}
