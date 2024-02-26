package com.example.clny.service;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.custom.EmailAlreadyInUseException;
import com.example.clny.exception.custom.NoAccountAssociatedWithEmailException;
import com.example.clny.exception.custom.WrongPasswordException;
import com.example.clny.mapper.UserMapper;
import com.example.clny.model.User;
import com.example.clny.repository.CredentialsRepository;
import com.example.clny.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final CredentialsRepository credentialsRepository;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CredentialsRepository credentialsRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.credentialsRepository = credentialsRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO register(UserDTO userDTO) throws Exception {
        if(userDTO == null) {
            throw new IllegalArgumentException("param userDTO cannot be null.");
        }

        if(credentialsRepository.existsByEmail(userDTO.getCredentials().getEmail())) {
            throw new EmailAlreadyInUseException();
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getCredentials().getPassword());
        userDTO.getCredentials().setPassword(encodedPassword);

        User user = userMapper.userDTOToUser(userDTO);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    public UserDTO login(CredentialsDTO credentialsDTO) throws Exception {
        if(credentialsDTO == null) {
            throw new IllegalArgumentException("param credentialsDTO cannot be null.");
        }

        if(!credentialsRepository.existsByEmail(credentialsDTO.getEmail())) {
            throw new NoAccountAssociatedWithEmailException();
        }

        User user = userRepository.findByCredentialsEmail(credentialsDTO.getEmail());

        if(!passwordEncoder.matches(credentialsDTO.getPassword(), user.getCredentials().getPassword())) {
            throw new WrongPasswordException();
        }

        return userMapper.userToUserDTO(user);
    }

}
