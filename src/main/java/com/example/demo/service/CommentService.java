package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Account;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;

@Component
@Transactional
public class CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private AccountRepository accountRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, AccountRepository accountRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieves all the comments that are currently in the database.
     * 
     * @return The list of comments.
     */
    public List<Comment> getAllComments() {
        return this.commentRepository.findAll();
    }

    /**
     * Retrieves all of the comments that are associated with a particular
     * account by the accounts id.
     * 
     * @param accountId The id of the account from which the comments are
     * associated with.
     * @return The list of comments.
     */
    public List<Comment> getAllCommentsByAccountId(long accountId) {
        return this.commentRepository.findAllCommentsByAccountId(accountId);
    }

    /**
     * Retrieves all of the comments that are associated with a particular
     * post by the posts id.
     * 
     * @param postId The if of the post from which the comments are associated
     * with.
     * @return The list of comments.
     */
    public List<Comment> getAllCommentsByPostId(long postId) {
        return this.commentRepository.findAllCommentsByPostId(postId);
    }

    /**
     * Adds a new comment to the database.
     * 
     * @param comment The comment to be added.
     * @return The comment that was added. Return null if it was unable to add
     * the comment.
     */
    public Comment addComment(Comment comment, long postId) {
        if(comment.getComment() == null || comment.getAccount() == null) {
            return null;
        }

        // This could be moved to a validation service file but
        // I just put it here for now to get it working. Moving it might 
        // make this design follow SRP better.
        Optional<Account> account = this.accountRepository.findById(comment.getAccount().getAccountId());
        Optional<Post> post = this.postRepository.findById(postId);

        if(!account.isPresent() || !post.isPresent( )) {
            return null;
        }
        
        post.get().getComments().add(comment);
        comment.setPost(post.get());

        return this.commentRepository.save(comment);
    }
    
    /**
     * Updated a comment that is currently in the database.
     * 
     * @param updatedComment The updated comment.
     * @return The comment that was updated. Returns null if no such comment
     * with the given id exists in the database.
     */
    public Comment updateComment(long postId, Comment updatedComment, String accountName, String password) {
        Optional<Comment> comment = this.commentRepository.findById(updatedComment.getCommentId());

        if(!comment.isPresent()) {
            return null;
        }

        Optional<Account> account = this.accountRepository.findAccountByAccountName(accountName);

        if(!account.isPresent()) {
            return null;
        }

        if(!account.get().getPassword().equals(password)) {
            return null;
        }

        Comment commentToUpdate = comment.get();

        if(commentToUpdate.getAccount() != account.get() && account.get().getRole() != Role.ADMIN) {
            return null;
        }

        if(updatedComment.getComment() != null) {
            commentToUpdate.setComment(updatedComment.getComment());
        }

        return this.commentRepository.save(commentToUpdate);
    }

    /**
     * Deletes a comment form the database.
     * 
     * @param comment The comment to be deleted.
     */
    public void deleteComment(long postId, Comment commentToDelete, String accountName, String password) {
        Optional<Post> post = this.postRepository.findById(postId);
        Optional<Comment> comment = this.commentRepository.findById(commentToDelete.getCommentId());

        if(!post.isPresent() || !comment.isPresent() || !post.get().getComments().contains(comment.get())) {
            return;
        }

        Optional<Account> account = this.accountRepository.findAccountByAccountName(accountName);

        if(!account.isPresent()) {
            return;
        }

        if(!account.get().getPassword().equals(password)) {
            return;
        }

        if(comment.get().getAccount() != account.get() && account.get().getRole() != Role.ADMIN) {
            return;
        }

        post.get().getComments().remove(comment.get());
        this.commentRepository.delete(comment.get());
    }
}