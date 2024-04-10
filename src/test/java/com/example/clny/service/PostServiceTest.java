package com.example.clny.service;

import com.example.clny.dto.PostDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.custom.EmptyPostException;
import com.example.clny.exception.custom.NoPostAuthorException;
import com.example.clny.exception.custom.PostTooLongException;
import com.example.clny.mapper.PostMapper;
import com.example.clny.model.Post;
import com.example.clny.model.User;
import com.example.clny.repository.PostRepository;
import com.example.clny.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostMapper postMapper;

    @Test
    void getPostsFromUserTest_HappyPath() {
        // Arrange
        Long userId = 1L;

        List<Post> posts = List.of(new Post("Test content", new ArrayList<>(), new User()));
        List<PostDTO> postDTOs = posts.stream()
                .map(post -> new PostDTO(post.getContent(), post.getImages(), new UserDTO()))
                .toList();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(postRepository.findByAuthorId(userId)).thenReturn(posts);
        when(postMapper.postToPostDTO(any(Post.class))).thenReturn(postDTOs.get(0));

        // Act
        List<PostDTO> result = postService.getPostsFromUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(postDTOs.size(), result.size());
    }

    @Test
    void getPostsFromUserTest_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            postService.getPostsFromUser(userId);
        }, "Expected getPostsFromUser() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void deletePostTest_HappyPath() {
        // Arrange
        Long postId = 1L;
        Long userId = 1L;

        User author = new User();
        author.setId(userId);

        Post post = new Post("Test content", new ArrayList<>(), author);
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.existsById(userId)).thenReturn(true);

        List<Post> remainingPosts = new ArrayList<>();
        when(postRepository.findByAuthorId(userId)).thenReturn(remainingPosts);

        // Act
        List<PostDTO> result = postService.deletePost(postId);

        // Assert
        assertNotNull(result);
        assertEquals(remainingPosts.size(), result.size());
    }

    @Test
    void deletePostTest_PostDoesNotExist() {
        // Arrange
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(postId);
        }, "Expected deletePost() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void createPostTest_HappyPath() throws Exception {
        // Arrange
        String content = "This is a test post";
        Long userId = 1L;

        UserDTO authorDTO = new UserDTO();
        authorDTO.setId(userId);

        PostDTO postDTO = new PostDTO(content, new ArrayList<>(), authorDTO);

        User author = new User();
        author.setId(userId);

        Post post = new Post(content, new ArrayList<>(), author);
        post.setId(1L);

        List<MultipartFile> files = new ArrayList<>();

        when(postMapper.postDTOToPost(postDTO)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(userRepository.existsById(userId)).thenReturn(true);

        List<Post> posts = List.of(post);
        when(postRepository.findByAuthorId(userId)).thenReturn(posts);

        // Act
        List<PostDTO> result = postService.createPost(postDTO, files);

        // Assert
        assertNotNull(result);
        assertEquals(posts.size(), result.size());
    }

    @Test
    void createPostTest_NullParam() {
        // Arrange
        List<MultipartFile> files = new ArrayList<>();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(null, files);
        }, "Expected createPost() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void createPostTest_EmptyPost() {
        // Arrange
        PostDTO postDTO = new PostDTO("", new ArrayList<>(), new UserDTO());
        List<MultipartFile> files = new ArrayList<>();

        // Act and Assert
        assertThrows(EmptyPostException.class, () -> {
            postService.createPost(postDTO, files);
        }, "Expected createPost() to throw EmptyPostException, but it didn't");
    }

    @Test
    void createPostTest_PostTooLong() {
        // Arrange
        String longContent = "a".repeat(251);
        PostDTO postDTO = new PostDTO(longContent, new ArrayList<>(), new UserDTO());
        List<MultipartFile> files = new ArrayList<>();

        // Act and Assert
        assertThrows(PostTooLongException.class, () -> {
            postService.createPost(postDTO, files);
        }, "Expected createPost() to throw PostTooLongException, but it didn't");
    }

    @Test
    void createPostTest_NoPostAuthor() {
        // Arrange
        PostDTO postDTO = new PostDTO("Valid content", new ArrayList<>(), null);
        List<MultipartFile> files = new ArrayList<>();

        // Act and Assert
        assertThrows(NoPostAuthorException.class, () -> {
            postService.createPost(postDTO, files);
        }, "Expected createPost() to throw NoPostAuthorException, but it didn't");
    }

}
