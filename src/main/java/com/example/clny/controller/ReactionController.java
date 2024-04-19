package com.example.clny.controller;

import com.example.clny.dto.ReactionDTO;
import com.example.clny.service.ReactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reaction")
public class ReactionController {

    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @GetMapping("/getReactions/{postId}")
    public ResponseEntity<List<ReactionDTO>> getPostReactions(@PathVariable Long postId) {
        return ResponseEntity.ok(reactionService.getPostReactions(postId));
    }

    @PostMapping("/addReaction")
    public ResponseEntity<List<ReactionDTO>> addReaction(@RequestBody ReactionDTO reactionDTO) throws Exception {
        return ResponseEntity.ok(reactionService.addReaction(reactionDTO));
    }

}
