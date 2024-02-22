package com.example.clny.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialsDTO {

    private Long id;

    private String email;

    private String password;

    public CredentialsDTO() {
    }

    public CredentialsDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
