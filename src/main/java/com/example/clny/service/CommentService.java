package com.example.clny.service;

import com.example.clny.dto.CommentDTO;
import com.example.clny.exception.custom.CommentTooLongException;
import com.example.clny.exception.custom.EmptyCommentException;
import com.example.clny.exception.custom.NoCommentAuthorException;
import com.example.clny.exception.custom.NoCommentPostException;
import com.example.clny.mapper.CommentMapper;
import com.example.clny.model.Comment;
import com.example.clny.repository.CommentRepository;
import com.example.clny.util.CommentNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public List<CommentNode> createComment(CommentDTO commentDTO) throws Exception {
        if(commentDTO == null) {
            throw new IllegalArgumentException("param commentDTO cannot be null.");
        }

        if(commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            throw new EmptyCommentException();
        }

        if(commentDTO.getContent().length() > 250) {
            throw new CommentTooLongException();
        }

        if(commentDTO.getAuthor() == null) {
            throw new NoCommentAuthorException();
        }

        if(commentDTO.getPost() == null) {
            throw new NoCommentPostException();
        }

        Comment comment = commentMapper.commentDTOToComment(commentDTO);
        commentRepository.save(comment);

        return getCommentsFromPost(commentDTO.getPost().getId());
    }

    public List<CommentNode> getCommentsFromPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        Map<Long, CommentNode> commentMap = new HashMap<>();

        for (Comment comment : comments) {
            CommentDTO commentDto = commentMapper.commentToCommentDTO(comment);
            commentMap.put(comment.getId(), new CommentNode(commentDto));
        }

        List<CommentNode> commentHierarchy = new ArrayList<>();
        for (Comment comment : comments) {
            CommentNode node = commentMap.get(comment.getId());
            Long parentCommentId = comment.getParentComment() != null ? comment.getParentComment().getId() : null;

            if (parentCommentId == null) {
                commentHierarchy.add(node);
            } else {
                CommentNode parentNode = commentMap.get(parentCommentId);
                if (parentNode != null) {
                    parentNode.addReply(node);
                }
            }
        }

        return commentHierarchy;
    }

}
