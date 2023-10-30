package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Post;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * 
     * @param accountId
     * @return
     */
    @Query("FROM post WHERE account.accountId = :accountId")
    List<Post> findPostsByAccountId(long accountId);
}
