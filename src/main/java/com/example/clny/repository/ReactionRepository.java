package com.example.clny.repository;

import com.example.clny.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findAllByPostId(Long postId);

    Optional<Reaction> findByAuthorIdAndPostId(Long authorId, Long postId);

}
