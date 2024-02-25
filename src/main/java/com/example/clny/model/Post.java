package com.example.clny.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String content;

    @ElementCollection
    private List<String> images;

    private final Date postDate = new Date();

    @NotNull
    @ManyToOne
    private User author;

    private boolean edited;

    public Post() {
    }

    public Post(String content, List<String> images, User author) {
        this.content = content;
        this.images = images;
        this.author = author;
    }

}
