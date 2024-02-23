package com.example.clny.mapper;

import com.example.clny.dto.CommentDTO;
import com.example.clny.model.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDTO commentToCommentDTO(Comment comment);

    Comment commentDTOToComment(CommentDTO commentDTO);

}
