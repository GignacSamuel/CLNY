package com.example.clny.service;

import com.example.clny.dto.PostDTO;
import com.example.clny.dto.ReactionDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.exception.custom.NoReactionAuthorException;
import com.example.clny.exception.custom.NoReactionPostException;
import com.example.clny.exception.custom.NoReactionTypeException;
import com.example.clny.mapper.ReactionMapper;
import com.example.clny.model.Post;
import com.example.clny.model.Reaction;
import com.example.clny.model.ReactionType;
import com.example.clny.model.User;
import com.example.clny.repository.PostRepository;
import com.example.clny.repository.ReactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReactionServiceTest {

    @InjectMocks
    private ReactionService reactionService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private ReactionMapper reactionMapper;

    @Test
    void getPostReactionsTest_HappyPath() {
        // Arrange
        Long postId = 1L;

        Post post = new Post();
        post.setId(1L);

        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);

        User user = new User();
        user.setId(1L);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        Reaction reaction = new Reaction(ReactionType.LIKE, user, post);
        ReactionDTO reactionDTO = new ReactionDTO(ReactionType.LIKE, userDTO, postDTO);

        when(postRepository.existsById(anyLong())).thenReturn(true);
        when(reactionRepository.findAllByPostId(anyLong())).thenReturn(List.of(reaction));
        when(reactionMapper.reactionToReactionDTO(any(Reaction.class))).thenReturn(reactionDTO);

        // Act
        List<ReactionDTO> result = reactionService.getPostReactions(postId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        ReactionDTO resultDTO = result.get(0);
        assertEquals(ReactionType.LIKE, resultDTO.getType());
        assertEquals(1L, resultDTO.getAuthor().getId());
        assertEquals(1L, resultDTO.getPost().getId());
    }

    @Test
    void getPostReactionsTest_PostDoesNotExist() {
        // Arrange
        Long postId = 1L;

        when(postRepository.existsById(anyLong())).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reactionService.getPostReactions(postId);
        }, "Expected getPostReactions() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void addReactionTest_HappyPath() throws Exception {
        // Arrange
        Long postId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Post post = new Post();
        post.setId(postId);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);

        PostDTO postDTO = new PostDTO();
        postDTO.setId(postId);

        ReactionDTO newReactionDTO = new ReactionDTO(ReactionType.LIKE, userDTO, postDTO);
        Reaction newReaction = new Reaction(ReactionType.LIKE, user, post);

        when(postRepository.existsById(anyLong())).thenReturn(true);
        when(reactionRepository.findByAuthorIdAndPostId(userId, postId)).thenReturn(Optional.empty());
        when(reactionMapper.reactionDTOToReaction(any(ReactionDTO.class))).thenReturn(newReaction);
        when(reactionRepository.save(any(Reaction.class))).thenReturn(newReaction);
        when(reactionRepository.findAllByPostId(postId)).thenReturn(List.of(newReaction));
        when(reactionMapper.reactionToReactionDTO(any(Reaction.class))).thenReturn(newReactionDTO);

        // Act
        List<ReactionDTO> result = reactionService.addReaction(newReactionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        ReactionDTO resultDTO = result.get(0);
        assertEquals(ReactionType.LIKE, resultDTO.getType());
        assertEquals(userId, resultDTO.getAuthor().getId());
        assertEquals(postId, resultDTO.getPost().getId());
    }

    @Test
    void addReactionTest_NullParam() {
        // Arrange
        ReactionDTO reactionDTO = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reactionService.addReaction(reactionDTO);
        }, "Expected addReaction() to throw IllegalArgumentException, but it didn't");
    }

    @Test
    void addReactionTest_NoReactionType() {
        // Arrange
        ReactionDTO reactionDTO = new ReactionDTO(null, new UserDTO(), new PostDTO());

        // Act and Assert
        assertThrows(NoReactionTypeException.class, () -> {
            reactionService.addReaction(reactionDTO);
        }, "Expected addReaction() to throw NoReactionTypeException, but it didn't");
    }

    @Test
    void addReactionTest_NoReactionAuthor() {
        // Arrange
        ReactionDTO reactionDTO = new ReactionDTO(ReactionType.LIKE, null, new PostDTO());

        // Act and Assert
        assertThrows(NoReactionAuthorException.class, () -> {
            reactionService.addReaction(reactionDTO);
        }, "Expected addReaction() to throw NoReactionAuthorException, but it didn't");
    }

    @Test
    void addReactionTest_NoReactionPost() {
        // Arrange
        ReactionDTO reactionDTO = new ReactionDTO(ReactionType.LIKE, new UserDTO(), null);

        // Act and Assert
        assertThrows(NoReactionPostException.class, () -> {
            reactionService.addReaction(reactionDTO);
        }, "Expected addReaction() to throw NoReactionPostException, but it didn't");
    }

}
