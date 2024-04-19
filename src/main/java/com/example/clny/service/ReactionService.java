package com.example.clny.service;

import com.example.clny.dto.ReactionDTO;
import com.example.clny.exception.custom.NoReactionAuthorException;
import com.example.clny.exception.custom.NoReactionPostException;
import com.example.clny.exception.custom.NoReactionTypeException;
import com.example.clny.mapper.ReactionMapper;
import com.example.clny.model.Reaction;
import com.example.clny.repository.PostRepository;
import com.example.clny.repository.ReactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReactionService {

    private final PostRepository postRepository;

    private final ReactionRepository reactionRepository;

    private final ReactionMapper reactionMapper;

    public ReactionService(PostRepository postRepository, ReactionRepository reactionRepository, ReactionMapper reactionMapper) {
        this.postRepository = postRepository;
        this.reactionRepository = reactionRepository;
        this.reactionMapper = reactionMapper;
    }

    public List<ReactionDTO> getPostReactions(Long postId) {
        if(!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("no post with id : " + postId);
        }

        return reactionRepository.findAllByPostId(postId).stream()
                .map(reactionMapper::reactionToReactionDTO)
                .collect(Collectors.toList());
    }

    public List<ReactionDTO> addReaction(ReactionDTO reactionDTO) throws Exception {
        if(reactionDTO == null) {
            throw new IllegalArgumentException("param reactionDTO cannot be null.");
        }

        if(reactionDTO.getType() == null) {
            throw new NoReactionTypeException();
        }

        if(reactionDTO.getAuthor() == null) {
            throw new NoReactionAuthorException();
        }

        if(reactionDTO.getPost() == null) {
            throw new NoReactionPostException();
        }

        Long authorId = reactionDTO.getAuthor().getId();
        Long postId = reactionDTO.getPost().getId();
        Optional<Reaction> existingReaction = reactionRepository.findByAuthorIdAndPostId(authorId, postId);

        if(existingReaction.isPresent()) {
            if(existingReaction.get().getType() == reactionDTO.getType()) {
                reactionRepository.delete(existingReaction.get());
                return getPostReactions(postId);
            }

            reactionRepository.delete(existingReaction.get());
        }

        Reaction reaction = reactionMapper.reactionDTOToReaction(reactionDTO);
        reactionRepository.save(reaction);

        return getPostReactions(postId);
    }

}
