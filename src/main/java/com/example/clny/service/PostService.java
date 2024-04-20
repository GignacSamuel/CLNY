package com.example.clny.service;

import com.example.clny.dto.PostDTO;
import com.example.clny.exception.custom.EmptyPostException;
import com.example.clny.exception.custom.NoPostAuthorException;
import com.example.clny.exception.custom.PostTooLongException;
import com.example.clny.mapper.PostMapper;
import com.example.clny.model.Post;
import com.example.clny.model.User;
import com.example.clny.repository.PostRepository;
import com.example.clny.repository.UserRepository;
import com.example.clny.util.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final PostMapper postMapper;

    private static final Path POST_PICTURE_DIRECTORY = Paths.get(System.getProperty("user.dir"), "frontend", "public", "uploads", "post-pictures");

    public PostService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    public List<PostDTO> createPost(PostDTO postDTO, List<MultipartFile> files) throws Exception {
        if(postDTO == null) {
            throw new IllegalArgumentException("param postDTO cannot be null.");
        }

        if(postDTO.getContent() == null || postDTO.getContent().trim().isEmpty()) {
            throw new EmptyPostException();
        }

        if(postDTO.getContent().length() > 250) {
            throw new PostTooLongException();
        }

        if(postDTO.getAuthor() == null) {
            throw new NoPostAuthorException();
        }

        List<String> images = new ArrayList<>();
        if(files != null) {
            for(MultipartFile file : files) {
                String webPath = FileUploadUtil.processFileUpload(file, POST_PICTURE_DIRECTORY);
                images.add(webPath);
            }
        }

        Post post = postMapper.postDTOToPost(postDTO);
        post.setImages(images);
        postRepository.save(post);

        return getPostsFromUser(postDTO.getAuthor().getId());
    }

    public List<PostDTO> getPostsFromUser(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("no user with id : " + userId);
        }

        List<Post> posts = postRepository.findByAuthorId(userId);

        return posts.stream()
                .map(postMapper::postToPostDTO)
                .collect(Collectors.toList());
    }

    public List<PostDTO> deletePost(Long postId) {
        Post toDelete = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("no post with id : " + postId));
        Long userId = toDelete.getAuthor().getId();

        deletePostPictures(toDelete);

        postRepository.delete(toDelete);
        return getPostsFromUser(userId);
    }

    public List<PostDTO> getFeedPosts(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("no user with id : " + userId);
        }

        List<Post> posts = postRepository.findAllByFollowing(userId);

        return posts.stream()
                .map(postMapper::postToPostDTO)
                .collect(Collectors.toList());
    }

    private void deletePostPictures(Post post) {
        for(String webPath : post.getImages()) {
            FileUploadUtil.deleteFileAtPath(webPath, POST_PICTURE_DIRECTORY);
        }
    }

}
