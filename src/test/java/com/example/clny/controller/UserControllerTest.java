package com.example.clny.controller;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.ProfileDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.GlobalExceptionHandler;
import com.example.clny.exception.custom.*;
import com.example.clny.filter.JwtAuthenticationFilter;
import com.example.clny.model.AuthenticationResponse;
import com.example.clny.service.JwtService;
import com.example.clny.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void registerTest_HappyPath() throws Exception {
        // Arrange
        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com","password");
        ProfileDTO profileDTO = new ProfileDTO("profilePicture","bannerPicture","bio");
        UserDTO userDTO = new UserDTO("firstName","lastName",credentialsDTO,profileDTO);

        String dummyToken = "dummyToken";

        AuthenticationResponse response = new AuthenticationResponse(dummyToken, userDTO);

        when(userService.register(any(UserDTO.class))).thenReturn(response);

        // Act and Assert
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void registerTest_EmailAlreadyInUse() throws Exception {
        // Arrange
        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com","password");
        ProfileDTO profileDTO = new ProfileDTO("profilePicture","bannerPicture","bio");
        UserDTO userDTO = new UserDTO("firstName","lastName",credentialsDTO,profileDTO);

        when(userService.register(any(UserDTO.class))).thenThrow(EmailAlreadyInUseException.class);

        // Act and Assert
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginTest_HappyPath() throws Exception {
        // Arrange
        String payload = "{\"email\":\"email@gmail.com\",\"password\":\"password\"}";

        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com","password");
        ProfileDTO profileDTO = new ProfileDTO("profilePicture","bannerPicture","bio");
        UserDTO userDTO = new UserDTO("firstName","lastName",credentialsDTO,profileDTO);

        String dummyToken = "dummyToken";

        AuthenticationResponse response = new AuthenticationResponse(dummyToken, userDTO);

        when(userService.login(any(CredentialsDTO.class))).thenReturn(response);

        // Act and Assert
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void loginTest_NoAccountAssociatedWithEmail() throws Exception {
        // Arrange
        String payload = "{\"email\":\"email@gmail.com\",\"password\":\"password\"}";

        when(userService.login(any(CredentialsDTO.class))).thenThrow(NoAccountAssociatedWithEmailException.class);

        // Act and Assert
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginTest_WrongPassword() throws Exception {
        // Arrange
        String payload = "{\"email\":\"email@gmail.com\",\"password\":\"password\"}";

        when(userService.login(any(CredentialsDTO.class))).thenThrow(WrongPasswordException.class);

        // Act and Assert
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfilePictureTest_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "image data".getBytes());
        String expectedWebPath = "/uploads/profile-pictures/newFileName.png";

        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com", "password");
        ProfileDTO profileDTO = new ProfileDTO(expectedWebPath, "bannerPicture", "bio");
        UserDTO userDTO = new UserDTO("firstName", "lastName", credentialsDTO, profileDTO);

        when(userService.updateProfilePicture(eq(userId), any(MultipartFile.class))).thenReturn(userDTO);

        // Act and Assert
        mockMvc.perform(multipart("/user/updateProfilePicture/{userId}", userId)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    void updateProfilePictureTest_EmptyFile() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile emptyFile = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, new byte[0]);

        when(userService.updateProfilePicture(eq(userId), any(MultipartFile.class))).thenThrow(EmptyFileException.class);

        // Act and Assert
        mockMvc.perform(multipart("/user/updateProfilePicture/{userId}", userId)
                        .file(emptyFile)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfilePictureTest_FileNotImage() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile nonImageFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "text data".getBytes());

        when(userService.updateProfilePicture(eq(userId), any(MultipartFile.class))).thenThrow(FileNotImageException.class);

        // Act and Assert
        mockMvc.perform(multipart("/user/updateProfilePicture/{userId}", userId)
                        .file(nonImageFile)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfilePictureTest_FileTooLarge() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile largeFile = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, new byte[1024 * 1024 * 11]);

        when(userService.updateProfilePicture(eq(userId), any(MultipartFile.class))).thenThrow(FileTooLargeException.class);

        // Act and Assert
        mockMvc.perform(multipart("/user/updateProfilePicture/{userId}", userId)
                        .file(largeFile)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBannerPictureTest_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "image data".getBytes());
        String expectedWebPath = "/uploads/banner-pictures/newFileName.png";

        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com", "password");
        ProfileDTO profileDTO = new ProfileDTO("profilePicture", expectedWebPath, "bio");
        UserDTO userDTO = new UserDTO("firstName", "lastName", credentialsDTO, profileDTO);

        when(userService.updateBannerPicture(eq(userId), any(MultipartFile.class))).thenReturn(userDTO);

        // Act and Assert
        mockMvc.perform(multipart("/user/updateBannerPicture/{userId}", userId)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    void updateBannerPictureTest_EmptyFile() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile emptyFile = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, new byte[0]);

        when(userService.updateBannerPicture(eq(userId), any(MultipartFile.class))).thenThrow(EmptyFileException.class);

        // Act and Assert
        mockMvc.perform(multipart("/user/updateBannerPicture/{userId}", userId)
                        .file(emptyFile)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBannerPictureTest_FileNotImage() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile nonImageFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "text data".getBytes());

        when(userService.updateBannerPicture(eq(userId), any(MultipartFile.class))).thenThrow(FileNotImageException.class);

        // Act and Assert
        mockMvc.perform(multipart("/user/updateBannerPicture/{userId}", userId)
                        .file(nonImageFile)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBannerPictureTest_FileTooLarge() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile largeFile = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, new byte[1024 * 1024 * 11]);

        when(userService.updateBannerPicture(eq(userId), any(MultipartFile.class))).thenThrow(FileTooLargeException.class);

        // Act and Assert
        mockMvc.perform(multipart("/user/updateBannerPicture/{userId}", userId)
                        .file(largeFile)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBiography_HappyPath() throws Exception {
        // Arrange
        Long userId = 1L;
        String newBio = "a new bio";

        CredentialsDTO credentialsDTO = new CredentialsDTO("email@gmail.com","password");
        ProfileDTO profileDTO = new ProfileDTO("profilePicture","bannerPicture",newBio);
        UserDTO userDTO = new UserDTO("firstName","lastName",credentialsDTO,profileDTO);

        when(userService.updateBiography(eq(userId), anyString())).thenReturn(userDTO);

        String payload = String.format("{\"newBiography\": \"%s\"}", newBio);

        // Act and Assert
        mockMvc.perform(put("/user/updateBiography/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void updateBiography_EmptyBiography() throws Exception {
        // Arrange
        Long userId = 1L;
        String newBio = ""; //empty

        when(userService.updateBiography(eq(userId), anyString())).thenThrow(EmptyBiographyException.class);

        String payload = String.format("{\"newBiography\": \"%s\"}", newBio);

        // Act and Assert
        mockMvc.perform(put("/user/updateBiography/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBiography_BiographyTooLong() throws Exception {
        // Arrange
        Long userId = 1L;
        String newBio = String.join("", Collections.nCopies(16, "1234567890")); // 160 characters

        when(userService.updateBiography(eq(userId), anyString())).thenThrow(BiographyTooLongException.class);

        String payload = String.format("{\"newBiography\": \"%s\"}", newBio);

        // Act and Assert
        mockMvc.perform(put("/user/updateBiography/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

}
