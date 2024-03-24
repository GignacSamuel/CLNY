package com.example.clny.repository;

import com.example.clny.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.credentials.email = :email")
    User findByCredentialsEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(concat('%', :search, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(concat('%', :search, '%')) OR " +
            "LOWER(concat(u.firstName, ' ', u.lastName)) LIKE LOWER(:search) OR " +
            "u.credentials.email = :search")
    List<User> searchByNameOrEmail(@Param("search") String search);

}
