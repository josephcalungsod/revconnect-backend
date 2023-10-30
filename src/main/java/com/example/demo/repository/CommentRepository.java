package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     *
     * @param accountId
     * @return
     */
    @Query("FROM Comment WHERE account.accountId = :accountId")
    List<Comment> findAllCommentsByAccountId(@PathVariable long accountId);

    /**
     * 
     * @param postId
     * @return
     */
    @Query("FROM Comment WHERE post.postId = :postId")
    List<Comment> findAllCommentsByPostId(@PathVariable long postId);

    /**
     * 
     * @param post
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT * FROM  comment WHERE post = :post")
    List<Comment> findAllCommentsByPost(@PathVariable Post post);
}
