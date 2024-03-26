package com.example.clny.controller;

import com.example.clny.service.UserFollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userfollow")
public class UserFollowController {

    private final UserFollowService userFollowService;

    public UserFollowController(UserFollowService userFollowService) {
        this.userFollowService = userFollowService;
    }

    @PostMapping("/follow")
    public ResponseEntity<List<Long>> followUser(@RequestBody Map<String, String> requestBody) throws Exception {
        return ResponseEntity.ok(userFollowService.followUser(
            Long.valueOf(requestBody.get("followerId")),
            Long.valueOf(requestBody.get("followedId"))
        ));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<List<Long>> unfollowUser(@RequestBody Map<String, String> requestBody) throws Exception {
        return ResponseEntity.ok(userFollowService.unfollowUser(
            Long.valueOf(requestBody.get("followerId")),
            Long.valueOf(requestBody.get("followedId"))
        ));
    }

    @GetMapping("/getFollowedIds/{userId}")
    public ResponseEntity<List<Long>> getAllFollowedIds(@PathVariable Long userId) {
        return ResponseEntity.ok(userFollowService.getAllFollowedIds(userId));
    }

}
