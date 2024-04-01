package com.example.clny.controller;

import com.example.clny.dto.PostDTO;
import com.example.clny.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/createPost")
    public ResponseEntity<PostDTO> createPost(@Validated @RequestPart("postDTO") PostDTO postDTO, @RequestPart("file") List<MultipartFile> files) throws Exception {
        return ResponseEntity.ok(postService.createPost(postDTO, files));
    }

}
