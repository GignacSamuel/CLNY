package com.example.clny.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private CredentialsDTO credentials;

    private ProfileDTO profile;

    private Date creationDate;

    public UserDTO() {
    }

    public UserDTO(String firstName, String lastName, CredentialsDTO credentials, ProfileDTO profile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.credentials = credentials;
        this.profile = profile;
    }

}
