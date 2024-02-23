package com.example.clny.service;

import com.example.clny.dto.UserDTO;
import com.example.clny.mapper.UserMapper;
import com.example.clny.model.User;
import com.example.clny.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO createUser(UserDTO userDTO) {

        // TODO : password hash and validation

        User user = userMapper.userDTOToUser(userDTO);
        return userMapper.userToUserDTO(userRepository.save(user));

    }

}
