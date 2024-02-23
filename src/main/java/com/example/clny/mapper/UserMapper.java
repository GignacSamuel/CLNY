package com.example.clny.mapper;

import com.example.clny.dto.UserDTO;
import com.example.clny.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);

}
