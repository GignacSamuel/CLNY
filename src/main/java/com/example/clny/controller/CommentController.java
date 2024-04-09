package com.example.clny.controller;

import com.example.clny.dto.CommentDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.service.CommentService;
import com.example.clny.util.CommentNode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/createComment")
    public ResponseEntity<List<CommentNode>> createComment(@RequestBody CommentDTO commentDTO) throws Exception {
        return ResponseEntity.ok(commentService.createComment(commentDTO));
    }

    @GetMapping("/getComments/{postId}")
    public ResponseEntity<List<CommentNode>> getCommentsFromPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsFromPost(postId));
    }

}
