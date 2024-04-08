package com.example.clny.service;

import com.example.clny.dto.CommentDTO;
import com.example.clny.exception.custom.CommentTooLongException;
import com.example.clny.exception.custom.EmptyCommentException;
import com.example.clny.exception.custom.NoCommentAuthorException;
import com.example.clny.exception.custom.NoCommentPostException;
import com.example.clny.mapper.CommentMapper;
import com.example.clny.model.Comment;
import com.example.clny.repository.CommentRepository;
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

    public List<List<CommentDTO>> createComment(CommentDTO commentDTO) throws Exception {
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

    public List<List<CommentDTO>> getCommentsFromPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        Map<Long, List<CommentDTO>> commentThreads = new HashMap<>();

        commentThreads.put(null, new ArrayList<>());

        for (Comment comment : comments) {
            CommentDTO commentDto = commentMapper.commentToCommentDTO(comment);
            Long parentCommentId = comment.getParentComment() != null ? comment.getParentComment().getId() : null;

            commentThreads.computeIfAbsent(parentCommentId, k -> new ArrayList<>()).add(commentDto);
        }

        List<List<CommentDTO>> commentHierarchy = new ArrayList<>();
        for (CommentDTO topLevelComment : commentThreads.get(null)) {
            List<CommentDTO> thread = new ArrayList<>();
            thread.add(topLevelComment);
            buildCommentThread(topLevelComment.getId(), commentThreads, thread);
            commentHierarchy.add(thread);
        }

        return commentHierarchy;
    }

    private void buildCommentThread(Long commentId, Map<Long, List<CommentDTO>> commentThreads, List<CommentDTO> thread) {
        List<CommentDTO> replies = commentThreads.get(commentId);
        if (replies != null) {
            for (CommentDTO reply : replies) {
                thread.add(reply);
                buildCommentThread(reply.getId(), commentThreads, thread);
            }
        }
    }

}
