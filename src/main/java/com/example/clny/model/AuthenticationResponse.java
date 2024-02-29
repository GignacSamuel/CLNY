package com.example.clny.model;

import com.example.clny.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {

    private String token;

    private UserDTO userDTO;

    public AuthenticationResponse(String token, UserDTO userDTO) {
        this.token = token;
        this.userDTO = userDTO;
    }

}
