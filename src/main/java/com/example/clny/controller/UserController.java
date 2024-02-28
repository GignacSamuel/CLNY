package com.example.clny.controller;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Validated @RequestBody UserDTO userDTO) throws Exception {
        UserDTO registeredUser = userService.register(userDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Validated @RequestBody CredentialsDTO credentialsDTO) throws Exception {
        UserDTO authenticatedUser = userService.login(credentialsDTO);
        return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
    }

}
