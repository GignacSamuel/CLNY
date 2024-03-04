package com.example.clny.service;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.ProfileDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.custom.EmailAlreadyInUseException;
import com.example.clny.exception.custom.NoAccountAssociatedWithEmailException;
import com.example.clny.exception.custom.WrongPasswordException;
import com.example.clny.mapper.UserMapper;
import com.example.clny.model.AuthenticationResponse;
import com.example.clny.model.Credentials;
import com.example.clny.model.Profile;
import com.example.clny.model.User;
import com.example.clny.repository.CredentialsRepository;
import com.example.clny.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CredentialsRepository credentialsRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void registerTest_HappyPath() throws Exception {
        // Arrange
        Credentials credentials = new Credentials("email@gmail.com","password");
        Profile profile = new Profile("profilePicture","bannerPicture","bio");
        User user = new User("firstName","lastName",credentials,profile);

        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com","password");
        ProfileDTO profileDTO = new ProfileDTO("profilePicture","bannerPicture","bio");
        UserDTO userDTO = new UserDTO("firstName","lastName",credentialsDTO,profileDTO);

        String dummyToken = "dummyToken";

        when(credentialsRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.userDTOToUser(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(dummyToken);

        // Act
        AuthenticationResponse response = userService.register(userDTO);

        // Assert
        assertNotNull(response);
        assertEquals(dummyToken, response.getToken());
    }

    @Test
    void registerTest_NullParam() {
        // Arrange
        UserDTO userDTO = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(userDTO);
        }, "Expected register() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void registerTest_EmailAlreadyInUse() {
        // Arrange
        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com","password");
        ProfileDTO profileDTO = new ProfileDTO("profilePicture","bannerPicture","bio");
        UserDTO userDTO = new UserDTO("firstName","lastName",credentialsDTO,profileDTO);

        when(credentialsRepository.existsByEmail(anyString())).thenReturn(true);

        // Act and Assert
        assertThrows(EmailAlreadyInUseException.class, () -> {
            userService.register(userDTO);
        }, "Expected register() to throw EmailAlreadyInUseException, but it didn't");
    }

    @Test
    void loginTest_HappyPath() throws Exception {
        // Arrange
        Credentials credentials = new Credentials("email@gmail.com","password");
        Profile profile = new Profile("profilePicture","bannerPicture","bio");
        User user = new User("firstName","lastName",credentials,profile);

        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com","password");

        String dummyToken = "dummyToken";

        when(credentialsRepository.existsByEmail(anyString())).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByCredentialsEmail(anyString())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(dummyToken);

        // Act
        AuthenticationResponse response = userService.login(credentialsDTO);

        // Assert
        assertNotNull(response);
        assertEquals(dummyToken, response.getToken());
    }

    @Test
    void loginTest_NullParam() {
        // Arrange
        CredentialsDTO credentialsDTO = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(credentialsDTO);
        }, "Expected login() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void loginTest_NoAccountAssociatedWithEmail() {
        // Arrange
        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com","password");

        when(credentialsRepository.existsByEmail(anyString())).thenReturn(false);

        // Act and Assert
        assertThrows(NoAccountAssociatedWithEmailException.class, () -> {
            userService.login(credentialsDTO);
        }, "Expected login() to throw NoAccountAssociatedWithEmailException, but it didn't");
    }

    @Test
    void loginTest_WrongPassword() {
        // Arrange
        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com","password");

        when(credentialsRepository.existsByEmail(anyString())).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        // Act and Assert
        assertThrows(WrongPasswordException.class, () -> {
            userService.login(credentialsDTO);
        }, "Expected login() to throw WrongPasswordException, but it didn't");
    }

}
