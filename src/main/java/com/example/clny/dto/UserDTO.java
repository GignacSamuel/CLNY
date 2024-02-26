package com.example.clny.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserDTO {

    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
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
