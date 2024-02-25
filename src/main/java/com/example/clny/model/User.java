package com.example.clny.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "\"user\"")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Credentials credentials;

    @OneToOne(cascade = CascadeType.ALL)
    private Profile profile;

    private final Date creationDate = new Date();

    public User() {
    }

    public User(String firstName, String lastName, Credentials credentials, Profile profile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.credentials = credentials;
        this.profile = profile;
    }

}
