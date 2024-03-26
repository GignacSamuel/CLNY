package com.example.clny.service;

import com.example.clny.exception.custom.AlreadyFollowingException;
import com.example.clny.exception.custom.NotFollowingException;
import com.example.clny.model.User;
import com.example.clny.model.UserFollow;
import com.example.clny.repository.UserFollowRepository;
import com.example.clny.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFollowService {

    private final UserRepository userRepository;

    private final UserFollowRepository userFollowRepository;

    public UserFollowService(UserRepository userRepository, UserFollowRepository userFollowRepository) {
        this.userRepository = userRepository;
        this.userFollowRepository = userFollowRepository;
    }

    public List<Long> getAllFollowedIds(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("no user with id : " + userId);
        }

        return userFollowRepository.findFollowedIdsByFollowerId(userId);
    }

    public List<Long> getAllFollowerIds(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("no user with id : " + userId);
        }

        return userFollowRepository.findFollowerIdsByFollowedId(userId);
    }

    public List<Long> followUser(Long followerId, Long followedId) throws Exception {
        User follower = userRepository.findById(followerId).orElseThrow(() -> new IllegalArgumentException("no user with id : " + followerId));
        User followed = userRepository.findById(followedId).orElseThrow(() -> new IllegalArgumentException("no user with id : " + followedId));

        if(userFollowRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {
            throw new AlreadyFollowingException();
        }

        UserFollow userFollow = new UserFollow(follower, followed);
        userFollowRepository.save(userFollow);

        return getAllFollowedIds(followerId);
    }

    public List<Long> unfollowUser(Long followerId, Long followedId) throws Exception {
        if(!userRepository.existsById(followerId)) {
            throw new IllegalArgumentException("no user with id : " + followerId);
        }

        if(!userRepository.existsById(followedId)) {
            throw new IllegalArgumentException("no user with id : " + followedId);
        }

        UserFollow userFollow = userFollowRepository.findByFollowerIdAndFollowedId(followerId, followedId);

        if(userFollow == null) {
            throw new NotFollowingException();
        }

        userFollowRepository.delete(userFollow);

        return getAllFollowedIds(followerId);
    }

}
