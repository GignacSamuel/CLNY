package com.example.clny.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PostDTO {

    private Long id;

    @NotBlank
    private String content;

    private List<String> images;

    private Date postDate;

    @NotNull
    private UserDTO author;

    private boolean edited;

    public PostDTO() {
    }

    public PostDTO(String content, List<String> images, UserDTO author) {
        this.content = content;
        this.images = images;
        this.author = author;
    }

}
