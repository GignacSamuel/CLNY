package com.example.clny.repository;

import com.example.clny.model.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    @Query("SELECT uf.followed.id FROM UserFollow uf WHERE uf.follower.id = :userId")
    List<Long> findFollowedIdsByFollowerId(@Param("userId") Long userId);

    @Query("SELECT uf.follower.id FROM UserFollow uf WHERE uf.followed.id = :userId")
    List<Long> findFollowerIdsByFollowedId(@Param("userId") Long userId);

    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    UserFollow findByFollowerIdAndFollowedId(Long followerId, Long followedId);

}
