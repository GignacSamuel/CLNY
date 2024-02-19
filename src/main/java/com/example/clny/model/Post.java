package com.example.clny.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @ElementCollection
    private List<String> images;

    private final Date postDate = new Date();

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
