package com.example.clny.mapper;

import com.example.clny.dto.NewFollowerNotificationDTO;
import com.example.clny.model.NewFollowerNotification;
import org.mapstruct.Mapper;

@Mapper
public interface NewFollowerNotificationMapper {

    NewFollowerNotificationDTO newFollowerNotificationToNewFollowerNotificationDTO(NewFollowerNotification newFollowerNotification);

    NewFollowerNotification newFollowerNotificationDTOToNewFollowerNotification(NewFollowerNotificationDTO newFollowerNotificationDTO);

}
