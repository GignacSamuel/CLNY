package com.example.clny.service;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.ProfileDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.custom.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @Test
    void updateProfilePictureTest_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;
        String expectedWebPath = "/uploads/profile-pictures/newFileName.png";
        MultipartFile file = mock(MultipartFile.class);

        Credentials credentials = new Credentials("email@gmail.com", "password");
        Profile profile = new Profile("profilePicture", "bannerPicture", "bio");
        User user = new User("firstName", "lastName", credentials, profile);

        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com", "password");
        ProfileDTO profileDTO = new ProfileDTO(expectedWebPath, "bannerPicture", "bio");
        UserDTO userDTO = new UserDTO("firstName", "lastName", credentialsDTO, profileDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getSize()).thenReturn(1024L); // 1KB
        when(file.getOriginalFilename()).thenReturn("originalFileName.png");
        doNothing().when(file).transferTo(any(Path.class));

        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        // Act
        UserDTO result = userService.updateProfilePicture(userId, file);

        // Assert
        assertNotNull(result);
        assertEquals(expectedWebPath, result.getProfile().getProfilePicture());
    }

    @Test
    void updateProfilePictureTest_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateProfilePicture(userId, file);
        }, "Expected updateProfilePicture() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void updateProfilePictureTest_EmptyFile() {
        // Arrange
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        Credentials credentials = new Credentials("email@gmail.com", "password");
        Profile profile = new Profile("profilePicture", "bannerPicture", "bio");
        User user = new User("firstName", "lastName", credentials, profile);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(file.isEmpty()).thenReturn(true);

        // Act and Assert
        assertThrows(EmptyFileException.class, () -> {
            userService.updateProfilePicture(userId, file);
        }, "Expected updateProfilePicture() to throw EmptyFileException, but it didn't");
    }

    @Test
    void updateProfilePictureTest_FileNotImage() {
        // Arrange
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        Credentials credentials = new Credentials("email@gmail.com", "password");
        Profile profile = new Profile("profilePicture", "bannerPicture", "bio");
        User user = new User("firstName", "lastName", credentials, profile);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("notimage");

        // Act and Assert
        assertThrows(FileNotImageException.class, () -> {
            userService.updateProfilePicture(userId, file);
        }, "Expected updateProfilePicture() to throw FileNotImageException, but it didn't");
    }

    @Test
    void updateProfilePictureTest_FileTooLarge() {
        // Arrange
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        Credentials credentials = new Credentials("email@gmail.com", "password");
        Profile profile = new Profile("profilePicture", "bannerPicture", "bio");
        User user = new User("firstName", "lastName", credentials, profile);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getSize()).thenReturn((long) (11 * 1024 * 1024)); // 11MB

        // Act and Assert
        assertThrows(FileTooLargeException.class, () -> {
            userService.updateProfilePicture(userId, file);
        }, "Expected updateProfilePicture() to throw FileTooLargeException, but it didn't");
    }

    @Test
    void updateBannerPictureTest_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;
        String expectedWebPath = "/uploads/banner-pictures/newFileName.png";
        MultipartFile file = mock(MultipartFile.class);

        Credentials credentials = new Credentials("email@gmail.com", "password");
        Profile profile = new Profile("profilePicture", "bannerPicture", "bio");
        User user = new User("firstName", "lastName", credentials, profile);

        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com", "password");
        ProfileDTO profileDTO = new ProfileDTO("profilePicture", expectedWebPath, "bio");
        UserDTO userDTO = new UserDTO("firstName", "lastName", credentialsDTO, profileDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getSize()).thenReturn(1024L); // 1KB
        when(file.getOriginalFilename()).thenReturn("originalFileName.png");
        doNothing().when(file).transferTo(any(Path.class));

        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        // Act
        UserDTO result = userService.updateBannerPicture(userId, file);

        // Assert
        assertNotNull(result);
        assertEquals(expectedWebPath, result.getProfile().getBannerPicture());
    }

    @Test
    void updateBannerPictureTest_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateBannerPicture(userId, file);
        }, "Expected updateBannerPicture() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void updateBannerPictureTest_EmptyFile() {
        // Arrange
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        Credentials credentials = new Credentials("email@gmail.com", "password");
        Profile profile = new Profile("profilePicture", "bannerPicture", "bio");
        User user = new User("firstName", "lastName", credentials, profile);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(file.isEmpty()).thenReturn(true);

        // Act and Assert
        assertThrows(EmptyFileException.class, () -> {
            userService.updateBannerPicture(userId, file);
        }, "Expected updateBannerPicture() to throw EmptyFileException, but it didn't");
    }

    @Test
    void updateBannerPictureTest_FileNotImage() {
        // Arrange
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        Credentials credentials = new Credentials("email@gmail.com", "password");
        Profile profile = new Profile("profilePicture", "bannerPicture", "bio");
        User user = new User("firstName", "lastName", credentials, profile);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("notimage");

        // Act and Assert
        assertThrows(FileNotImageException.class, () -> {
            userService.updateBannerPicture(userId, file);
        }, "Expected updateBannerPicture() to throw FileNotImageException, but it didn't");
    }

    @Test
    void updateBannerPictureTest_FileTooLarge() {
        // Arrange
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        Credentials credentials = new Credentials("email@gmail.com", "password");
        Profile profile = new Profile("profilePicture", "bannerPicture", "bio");
        User user = new User("firstName", "lastName", credentials, profile);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getSize()).thenReturn((long) (11 * 1024 * 1024)); // 11MB

        // Act and Assert
        assertThrows(FileTooLargeException.class, () -> {
            userService.updateBannerPicture(userId, file);
        }, "Expected updateBannerPicture() to throw FileTooLargeException, but it didn't");
    }

    @Test
    void updateBiography_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;
        String newBio = "a new bio";

        Credentials credentials = new Credentials("email@gmail.com","password");
        Profile profile = new Profile("profilePicture","bannerPicture","bio");
        User user = new User("firstName","lastName",credentials,profile);

        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com","password");
        ProfileDTO profileDTO = new ProfileDTO("profilePicture","bannerPicture",newBio);
        UserDTO userDTO = new UserDTO("firstName","lastName",credentialsDTO,profileDTO);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        // Act
        UserDTO result = userService.updateBiography(userId, newBio);

        // Assert
        assertNotNull(result);
        assertEquals(newBio, result.getProfile().getBiography());
    }

    @Test
    void updateBiography_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        String newBio = "a new bio";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateBiography(userId, newBio);
        }, "Expected updateBiography() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void updateBiography_EmptyBiography() {
        // Arrange
        Long userId = 1L;
        String newBio = ""; //empty

        Credentials credentials = new Credentials("email@gmail.com","password");
        Profile profile = new Profile("profilePicture","bannerPicture","bio");
        User user = new User("firstName","lastName",credentials,profile);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(EmptyBiographyException.class, () -> {
            userService.updateBiography(userId, newBio);
        }, "Expected updateBiography() to throw EmptyBiographyException, but it didn't");
    }

    @Test
    void updateBiography_BiographyTooLong() {
        // Arrange
        Long userId = 1L;
        String newBio = String.join("", Collections.nCopies(16, "1234567890")); // 160 characters

        Credentials credentials = new Credentials("email@gmail.com","password");
        Profile profile = new Profile("profilePicture","bannerPicture","bio");
        User user = new User("firstName","lastName",credentials,profile);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(BiographyTooLongException.class, () -> {
            userService.updateBiography(userId, newBio);
        }, "Expected updateBiography() to throw BiographyTooLongException, but it didn't");
    }

    @Test
    void searchUserTest_HappyPath() throws Exception {
        // Arrange
        String searchString = "Doe";

        Credentials credentials1 = new Credentials("email@gmail.com","password");
        Profile profile1 = new Profile("profilePicture","bannerPicture","bio");
        User user1 = new User("John","Doe",credentials1,profile1);

        Credentials credentials2 = new Credentials("email@gmail.com","password");
        Profile profile2 = new Profile("profilePicture","bannerPicture","bio");
        User user2 = new User("Jane","Doe",credentials2,profile2);

        CredentialsDTO credentialsDTO1 = new CredentialsDTO("email@gmail.com","password");
        ProfileDTO profileDTO1 = new ProfileDTO("profilePicture","bannerPicture","bio");
        UserDTO userDTO1 = new UserDTO("John","Doe",credentialsDTO1,profileDTO1);

        CredentialsDTO credentialsDTO2 = new CredentialsDTO("email@gmail.com","password");
        ProfileDTO profileDTO2 = new ProfileDTO("profilePicture","bannerPicture","bio");
        UserDTO userDTO2 = new UserDTO("Jane","Doe",credentialsDTO2,profileDTO2);

        List<User> users = Arrays.asList(user1, user2);
        List<UserDTO> userDTOS = Arrays.asList(userDTO1, userDTO2);

        when(userRepository.searchByNameOrEmail(anyString())).thenReturn(users);
        when(userMapper.userToUserDTO(user1)).thenReturn(userDTO1);
        when(userMapper.userToUserDTO(user2)).thenReturn(userDTO2);

        // Act
        List<UserDTO> result = userService.searchUser(searchString);

        // Assert
        assertNotNull(result);
        assertEquals(result, userDTOS);
    }

    @Test
    void searchUserTest_EmptySearchString() {
        // Arrange
        String searchString = ""; //empty

        // Act and Assert
        assertThrows(EmptySearchStringException.class, () -> {
            userService.searchUser(searchString);
        }, "Expected searchUser() to throw EmptySearchStringException, but it didn't");
    }

}
