package com.example.clny.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String profilePicture;

    private String bannerPicture;

    private String biography;

    public Profile() {
    }

    public Profile(String profilePicture, String bannerPicture, String biography) {
        this.profilePicture = profilePicture;
        this.bannerPicture = bannerPicture;
        this.biography = biography;
    }

}
