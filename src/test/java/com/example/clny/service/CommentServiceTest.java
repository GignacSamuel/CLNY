package com.example.clny.service;

import com.example.clny.dto.CommentDTO;
import com.example.clny.dto.PostDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.custom.CommentTooLongException;
import com.example.clny.exception.custom.EmptyCommentException;
import com.example.clny.exception.custom.NoCommentAuthorException;
import com.example.clny.exception.custom.NoCommentPostException;
import com.example.clny.mapper.CommentMapper;
import com.example.clny.model.Comment;
import com.example.clny.repository.CommentRepository;
import com.example.clny.util.CommentNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Test
    void getCommentsFromPostTest_HappyPath() {
        // Arrange
        Long postId = 1L;

        Comment parentComment = new Comment();
        parentComment.setId(1L);

        Comment childComment = new Comment();
        childComment.setId(2L);

        childComment.setParentComment(parentComment);

        List<Comment> comments = Arrays.asList(parentComment, childComment);

        CommentDTO parentCommentDTO = new CommentDTO();
        parentCommentDTO.setId(1L);
        CommentDTO childCommentDTO = new CommentDTO();
        childCommentDTO.setId(2L);

        when(commentRepository.findByPostId(postId)).thenReturn(comments);
        when(commentMapper.commentToCommentDTO(parentComment)).thenReturn(parentCommentDTO);
        when(commentMapper.commentToCommentDTO(childComment)).thenReturn(childCommentDTO);

        // Act
        List<CommentNode> result = commentService.getCommentsFromPost(postId);

        // Assert
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getComment().getId(), parentCommentDTO.getId());
        assertEquals(result.get(0).getReplies().size(), 1);
        assertEquals(result.get(0).getReplies().get(0).getComment().getId(), childCommentDTO.getId());
    }

    @Test
    void createCommentTest_HappyPath() throws Exception {
        // Arrange
        Long postId = 1L;
        Long commentId = 1L;
        String content = "Test comment";

        UserDTO author = new UserDTO();
        PostDTO post = new PostDTO();
        post.setId(postId);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentId);
        commentDTO.setContent(content);
        commentDTO.setAuthor(author);
        commentDTO.setPost(post);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setContent(content);

        when(commentMapper.commentDTOToComment(commentDTO)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);

        when(commentRepository.findByPostId(postId)).thenReturn(List.of(comment));
        when(commentMapper.commentToCommentDTO(comment)).thenReturn(commentDTO);

        // Act
        List<CommentNode> result = commentService.createComment(commentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commentId, result.get(0).getComment().getId());
    }

    @Test
    void createCommentTest_NullParam() {
        // Arrange
        CommentDTO commentDTO = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.createComment(commentDTO);
        }, "Expected createComment() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void createCommentTest_EmptyComment() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("   ");

        // Act and Assert
        assertThrows(EmptyCommentException.class, () -> {
            commentService.createComment(commentDTO);
        }, "Expected createComment() to throw EmptyCommentException, but it didn't");
    }

    @Test
    void createCommentTest_CommentTooLong() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        String longComment = "a".repeat(251);
        commentDTO.setContent(longComment);

        // Act and Assert
        assertThrows(CommentTooLongException.class, () -> {
            commentService.createComment(commentDTO);
        }, "Expected createComment() to throw CommentTooLongException, but it didn't");
    }

    @Test
    void createCommentTest_NoCommentAuthor() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Valid content");
        commentDTO.setAuthor(null);

        // Act and Assert
        assertThrows(NoCommentAuthorException.class, () -> {
            commentService.createComment(commentDTO);
        }, "Expected createComment() to throw NoCommentAuthorException, but it didn't");
    }

    @Test
    void createCommentTest_NoCommentPost() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Valid content");
        commentDTO.setAuthor(new UserDTO());
        commentDTO.setPost(null);

        // Act and Assert
        assertThrows(NoCommentPostException.class, () -> {
            commentService.createComment(commentDTO);
        }, "Expected createComment() to throw NoCommentPostException, but it didn't");
    }

}
