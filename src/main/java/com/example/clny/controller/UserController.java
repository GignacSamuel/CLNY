package com.example.clny.controller;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.model.AuthenticationResponse;
import com.example.clny.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Validated @RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity.ok(userService.register(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Validated @RequestBody CredentialsDTO credentialsDTO) throws Exception {
        return ResponseEntity.ok(userService.login(credentialsDTO));
    }

    @PutMapping("/updateProfilePicture/{userId}")
    public ResponseEntity<UserDTO> updateProfilePicture(@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(userService.updateProfilePicture(userId, file));
    }

}
