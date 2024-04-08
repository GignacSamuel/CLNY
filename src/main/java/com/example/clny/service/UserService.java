package com.example.clny.service;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.custom.*;
import com.example.clny.mapper.UserMapper;
import com.example.clny.model.AuthenticationResponse;
import com.example.clny.model.User;
import com.example.clny.repository.CredentialsRepository;
import com.example.clny.repository.UserRepository;
import com.example.clny.util.FileUploadUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        String webPath = FileUploadUtil.processFileUpload(file, PROFILE_PICTURE_DIRECTORY);

        deletePreviousProfilePicture(user);

        user.getProfile().setProfilePicture(webPath);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    public UserDTO updateBannerPicture(Long userId, MultipartFile file) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("no user with id : " + userId));
        String webPath = FileUploadUtil.processFileUpload(file, BANNER_PICTURE_DIRECTORY);

        deletePreviousBannerPicture(user);

        user.getProfile().setBannerPicture(webPath);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    private void deletePreviousProfilePicture(User user) {
        String webPath = user.getProfile().getProfilePicture();
        FileUploadUtil.deleteFileAtPath(webPath, PROFILE_PICTURE_DIRECTORY);
    }

    private void deletePreviousBannerPicture(User user) {
        String webPath = user.getProfile().getBannerPicture();
        FileUploadUtil.deleteFileAtPath(webPath, BANNER_PICTURE_DIRECTORY);
    }

    public UserDTO updateBiography(Long userId, String newBiography) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("no user with id : " + userId));

        if (newBiography == null || newBiography.trim().isEmpty()) {
            throw new EmptyBiographyException();
        }

        if (newBiography.length() > 150) {
            throw new BiographyTooLongException();
        }

        user.getProfile().setBiography(newBiography);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    public List<UserDTO> searchUser(String searchString) throws Exception {
        if (searchString == null || searchString.trim().isEmpty()) {
            throw new EmptySearchStringException();
        }

        List<User> users = userRepository.searchByNameOrEmail(searchString);
        return users.stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

}
