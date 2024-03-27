package com.example.clny.service;

import com.example.clny.exception.custom.AlreadyFollowingException;
import com.example.clny.exception.custom.NotFollowingException;
import com.example.clny.model.Credentials;
import com.example.clny.model.Profile;
import com.example.clny.model.User;
import com.example.clny.model.UserFollow;
import com.example.clny.repository.UserFollowRepository;
import com.example.clny.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserFollowServiceTest {

    @InjectMocks
    private UserFollowService userFollowService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFollowRepository userFollowRepository;

    @Test
    void getAllFollowedIdsTest_HappyPath() {
        // Arrange
        Long userId = 1L;
        List<Long> follows = Arrays.asList(2L,3L);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userFollowRepository.findFollowedIdsByFollowerId(anyLong())).thenReturn(follows);

        // Act
        List<Long> result = userFollowService.getAllFollowedIds(userId);

        // Assert
        assertNotNull(result);
        assertEquals(result, follows);
    }

    @Test
    void getAllFollowedIdsTest_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById(anyLong())).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userFollowService.getAllFollowedIds(userId);
        }, "Expected getAllFollowedIds() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void getAllFollowerIdsTest_HappyPath() {
        // Arrange
        Long userId = 1L;
        List<Long> followers = Arrays.asList(2L,3L);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userFollowRepository.findFollowerIdsByFollowedId(anyLong())).thenReturn(followers);

        // Act
        List<Long> result = userFollowService.getAllFollowerIds(userId);

        // Assert
        assertNotNull(result);
        assertEquals(result, followers);
    }

    @Test
    void getAllFollowerIdsTest_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById(anyLong())).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userFollowService.getAllFollowerIds(userId);
        }, "Expected getAllFollowerIds() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void followUserTest_HappyPath() throws Exception {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;

        List<Long> follows = List.of(2L);

        Credentials credentials1 = new Credentials("email@gmail.com","password");
        Profile profile1 = new Profile("profilePicture","bannerPicture","bio");
        User follower = new User("firstName","lastName",credentials1,profile1);

        follower.setId(followerId);

        Credentials credentials2 = new Credentials("email@gmail.com","password");
        Profile profile2 = new Profile("profilePicture","bannerPicture","bio");
        User followed = new User("firstName","lastName",credentials2,profile2);

        followed.setId(followedId);

        UserFollow userFollow = new UserFollow(follower, followed);

        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(followedId)).thenReturn(Optional.of(followed));

        when(userFollowRepository.existsByFollowerIdAndFollowedId(anyLong(),anyLong())).thenReturn(false);

        when(userFollowRepository.save(any(UserFollow.class))).thenReturn(userFollow);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userFollowRepository.findFollowedIdsByFollowerId(anyLong())).thenReturn(follows);

        // Act
        List<Long> result = userFollowService.followUser(followerId, followedId);

        // Assert
        assertNotNull(result);
        assertEquals(result, follows);
    }

    @Test
    void followUserTest_UserDoesNotExist() {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userFollowService.followUser(followerId, followedId);
        }, "Expected followUser() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void followUserTest_AlreadyFollowing() {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;

        Credentials credentials1 = new Credentials("email@gmail.com","password");
        Profile profile1 = new Profile("profilePicture","bannerPicture","bio");
        User follower = new User("firstName","lastName",credentials1,profile1);

        follower.setId(followerId);

        Credentials credentials2 = new Credentials("email@gmail.com","password");
        Profile profile2 = new Profile("profilePicture","bannerPicture","bio");
        User followed = new User("firstName","lastName",credentials2,profile2);

        followed.setId(followedId);

        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(followedId)).thenReturn(Optional.of(followed));

        when(userFollowRepository.existsByFollowerIdAndFollowedId(anyLong(),anyLong())).thenReturn(true);

        // Act and Assert
        assertThrows(AlreadyFollowingException.class, () -> {
            userFollowService.followUser(followerId, followedId);
        }, "Expected followUser() to throw AlreadyFollowingException, but it didn't");
    }

    @Test
    void unfollowUserTest_HappyPath() throws Exception {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;

        List<Long> follows = List.of();

        Credentials credentials1 = new Credentials("email@gmail.com","password");
        Profile profile1 = new Profile("profilePicture","bannerPicture","bio");
        User follower = new User("firstName","lastName",credentials1,profile1);

        follower.setId(followerId);

        Credentials credentials2 = new Credentials("email@gmail.com","password");
        Profile profile2 = new Profile("profilePicture","bannerPicture","bio");
        User followed = new User("firstName","lastName",credentials2,profile2);

        followed.setId(followedId);

        UserFollow userFollow = new UserFollow(follower, followed);

        when(userRepository.existsById(anyLong())).thenReturn(true);

        when(userFollowRepository.findByFollowerIdAndFollowedId(anyLong(),anyLong())).thenReturn(userFollow);
        doNothing().when(userFollowRepository).delete(any(UserFollow.class));

        when(userFollowRepository.findFollowedIdsByFollowerId(anyLong())).thenReturn(follows);

        // Act
        List<Long> result = userFollowService.unfollowUser(followerId, followedId);

        // Assert
        assertNotNull(result);
        assertEquals(result, follows);
    }

    @Test
    void unfollowUserTest_UserDoesNotExist() {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;

        when(userRepository.existsById(anyLong())).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userFollowService.unfollowUser(followerId, followedId);
        }, "Expected unfollowUser() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void unfollowUserTest_NotFollowing() {
        // Arrange
        Long followerId = 1L;
        Long followedId = 2L;

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userFollowRepository.findByFollowerIdAndFollowedId(anyLong(),anyLong())).thenReturn(null);

        // Act and Assert
        assertThrows(NotFollowingException.class, () -> {
            userFollowService.unfollowUser(followerId, followedId);
        }, "Expected unfollowUser() to throw NotFollowingException, but it didn't");
    }

}
