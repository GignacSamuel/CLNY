package com.example.clny.mapper;

import com.example.clny.dto.UserFollowDTO;
import com.example.clny.model.UserFollow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserFollowMapper {

    UserFollowDTO userFollowToUserFollowDTO(UserFollow userFollow);

    UserFollow userFollowDTOToUserFollow(UserFollowDTO userFollowDTO);

}
