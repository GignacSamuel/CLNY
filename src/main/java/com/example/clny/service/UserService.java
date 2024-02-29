package com.example.clny.service;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.custom.EmailAlreadyInUseException;
import com.example.clny.exception.custom.NoAccountAssociatedWithEmailException;
import com.example.clny.exception.custom.WrongPasswordException;
import com.example.clny.mapper.UserMapper;
import com.example.clny.model.AuthenticationResponse;
import com.example.clny.model.User;
import com.example.clny.repository.CredentialsRepository;
import com.example.clny.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final CredentialsRepository credentialsRepository;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, CredentialsRepository credentialsRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.credentialsRepository = credentialsRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(UserDTO userDTO) throws Exception {
        if(userDTO == null) {
            throw new IllegalArgumentException("param userDTO cannot be null.");
        }

        if(credentialsRepository.existsByEmail(userDTO.getCredentials().getEmail())) {
            throw new EmailAlreadyInUseException();
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getCredentials().getPassword());
        userDTO.getCredentials().setPassword(encodedPassword);

        User user = userMapper.userDTOToUser(userDTO);
        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token, userMapper.userToUserDTO(savedUser));
    }

    public AuthenticationResponse login(CredentialsDTO credentialsDTO) throws Exception {
        if(credentialsDTO == null) {
            throw new IllegalArgumentException("param credentialsDTO cannot be null.");
        }

        if(!credentialsRepository.existsByEmail(credentialsDTO.getEmail())) {
            throw new NoAccountAssociatedWithEmailException();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentialsDTO.getEmail(),
                            credentialsDTO.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new WrongPasswordException();
        }

        User user = userRepository.findByCredentialsEmail(credentialsDTO.getEmail());

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token, userMapper.userToUserDTO(user));
    }

}
