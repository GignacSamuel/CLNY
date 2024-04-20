package com.example.clny.controller;

import com.example.clny.dto.PostDTO;
import com.example.clny.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<PostDTO>> createPost(@RequestPart("postDTO") PostDTO postDTO, @RequestPart(name = "file", required = false) List<MultipartFile> files) throws Exception {
        return ResponseEntity.ok(postService.createPost(postDTO, files));
    }

    @GetMapping("/getPosts/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsFromUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsFromUser(userId));
    }

    @DeleteMapping("/deletePost/{postId}")
    public ResponseEntity<List<PostDTO>> deletePost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.deletePost(postId));
    }

    @GetMapping("/getFeed/{userId}")
    public ResponseEntity<List<PostDTO>> getFeedPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getFeedPosts(userId));
    }

}
