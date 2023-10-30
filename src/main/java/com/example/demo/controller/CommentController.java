package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Comment Controller constructor
 */

@CrossOrigin(origins = {"http://localhost:3000", "http://revconnect.azurewebsites.net"})
@RestController
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Returns all comments
     * 
     * @return
     */
    @GetMapping("/comment")
    public List<Comment> getAllComments() {
        return this.commentService.getAllComments();
    }

    /**
     * Get all comments by Account ID
     * 
     * @param id The id of the account.
     * @return The list of comments by that account.
     */
    @GetMapping("account/{id}/comment")
    public List<Comment> getAllCommentsByAccount(@PathVariable long id) {
        return this.commentService.getAllCommentsByAccountId(id);
    }

    /**
     * Get all of the comments from a post by the post's id.
     * 
     * @param id The id of the post.
     * @return The list of comments for that post.
     */
    @GetMapping("post/{id}/comment")
    public List<Comment> getAllCommentsByPostId(@PathVariable long id) {
        return this.commentService.getAllCommentsByPostId(id);
    }

    /**
     * Adds a new comment from an account to a post by the posts id as well as
     * the accounts id.
     * 
     * Body: 
     * {
     *   "comment": "Some comment",
     *   "account": {
     *     "accountId": ID of the account making the comment
     *   }
     * }
     * 
     * @param id The id of the post.
     * @param comment The comment to be posted.
     * @return The comment that was posted.
     */
    @PostMapping("post/{id}/comment")
    public Comment addComment(@PathVariable long id, @RequestBody Comment comment) {
        return this.commentService.addComment(comment, id);
    }

    /**
     * Updates/edits an existing comment.
     * 
     * @param id The id of the post.
     * @param comment The updated comment.
     * @param accountName The accountName that is initiating the request.
     * @param password The password of that account.
     * @return The updated comment.
     *
     */
    @PutMapping("post/{id}/comment")
    public Comment updateComment(@PathVariable long id, @RequestBody Comment comment, 
      @RequestHeader("account-name") String accountName, 
      @RequestHeader("password") String password) {
        return this.commentService.updateComment(id, comment, accountName, password);
    }

    /**
     * Deletes a comment from a specific post.
     * 
     * @param id The id of the post.
     * @param comment The comment to be deleted.
     * @param accountName The accountName that is initiating the request.
     * @param password The password of that account.
     */
    @DeleteMapping("post/{id}/comment")
    public void deleteComment(@PathVariable long id, @RequestBody Comment comment,
      @RequestHeader("account-name") String accountName, 
      @RequestHeader("password") String password) {
        this.commentService.deleteComment(id, comment, accountName, password);
    }
}
