package com.example.clny.service;

import com.example.clny.dto.PostDTO;
import com.example.clny.exception.custom.EmptyPostException;
import com.example.clny.exception.custom.NoPostAuthorException;
import com.example.clny.exception.custom.PostTooLongException;
import com.example.clny.mapper.PostMapper;
import com.example.clny.model.Post;
import com.example.clny.repository.PostRepository;
import com.example.clny.util.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private static final Path POST_PICTURE_DIRECTORY = Paths.get(System.getProperty("user.dir"), "frontend", "public", "uploads", "post-pictures");

    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public PostDTO createPost(PostDTO postDTO, List<MultipartFile> files) throws Exception {
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
        for(MultipartFile file : files) {
            String webPath = FileUploadUtil.processFileUpload(file, POST_PICTURE_DIRECTORY);
            images.add(webPath);
        }

        Post post = postMapper.postDTOToPost(postDTO);
        post.setImages(images);

        return postMapper.postToPostDTO(postRepository.save(post));
    }

}
