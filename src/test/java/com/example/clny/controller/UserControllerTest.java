package com.example.clny.controller;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.ProfileDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.GlobalExceptionHandler;
import com.example.clny.exception.custom.EmailAlreadyInUseException;
import com.example.clny.exception.custom.NoAccountAssociatedWithEmailException;
import com.example.clny.exception.custom.WrongPasswordException;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}
