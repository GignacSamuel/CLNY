package com.example.clny.repository;

import com.example.clny.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthorId(Long authorId);

    @Query("SELECT p FROM Post p JOIN UserFollow uf ON p.author = uf.followed WHERE uf.follower.id = :userId")
    List<Post> findAllByFollowing(@Param("userId") Long userId);

}
