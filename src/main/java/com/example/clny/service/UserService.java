package com.example.clny.service;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.custom.EmailAlreadyInUseException;
import com.example.clny.exception.custom.EmptyFileException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final CredentialsRepository credentialsRepository;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private static final Path PROFILE_PICTURE_DIRECTORY = Paths.get(System.getProperty("user.dir"), "frontend", "public", "uploads", "profile-pictures");

    private static final Path BANNER_PICTURE_DIRECTORY = Paths.get(System.getProperty("user.dir"), "frontend", "public", "uploads", "banner-pictures");

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

    public UserDTO updateProfilePicture(Long userId, MultipartFile file) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("no user with id : " + userId));
        String webPath = processFileUpload(file, PROFILE_PICTURE_DIRECTORY);
        user.getProfile().setProfilePicture(webPath);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    public UserDTO updateBannerPicture(Long userId, MultipartFile file) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("no user with id : " + userId));
        String webPath = processFileUpload(file, BANNER_PICTURE_DIRECTORY);
        user.getProfile().setBannerPicture(webPath);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    private String processFileUpload(MultipartFile file, Path directory) throws Exception {
        if (file.isEmpty()) {
            throw new EmptyFileException();
        }

        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + fileType;

        Path storePath = directory.resolve(newFileName);
        file.transferTo(storePath);

        return getWebPath(storePath);
    }

    private String getWebPath(Path storePath) {
        return storePath.toString().substring(storePath.toString().indexOf("/uploads"));
    }

}
