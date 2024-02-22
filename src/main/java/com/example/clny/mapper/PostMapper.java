package com.example.clny.mapper;

import com.example.clny.dto.PostDTO;
import com.example.clny.model.Post;
import org.mapstruct.Mapper;

@Mapper
public interface PostMapper {

    PostDTO postToPostDTO(Post post);

    Post postDTOToPost(PostDTO postDTO);

}
