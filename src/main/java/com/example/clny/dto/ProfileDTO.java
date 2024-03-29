package com.example.clny.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileDTO {

    private Long id;

    private String profilePicture;

    private String bannerPicture;

    private String biography;

    public ProfileDTO() {
    }

    public ProfileDTO(String profilePicture, String bannerPicture, String biography) {
        this.profilePicture = profilePicture;
        this.bannerPicture = bannerPicture;
        this.biography = biography;
    }

}
