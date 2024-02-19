package com.example.clny.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

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
