package com.example.clny.mapper;

import com.example.clny.dto.ReactionDTO;
import com.example.clny.model.Reaction;
import org.mapstruct.Mapper;

@Mapper
public interface ReactionMapper {

    ReactionDTO reactionToReactionDTO(Reaction reaction);

    Reaction reactionDTOToReaction(ReactionDTO reactionDTO);

}
