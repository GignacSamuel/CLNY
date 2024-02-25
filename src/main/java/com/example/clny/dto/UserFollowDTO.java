package com.example.clny.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserFollowDTO {

    private Long id;

    @NotNull
    private UserDTO follower;

    @NotNull
    private UserDTO followed;

    private Date followDate;

    public UserFollowDTO() {
    }

    public UserFollowDTO(UserDTO follower, UserDTO followed) {
        this.follower = follower;
        this.followed = followed;
    }

}
