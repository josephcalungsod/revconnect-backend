package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://revconnect.azurewebsites.net"})
@RestController
public class PostController {
    private PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    /**
     * Retrieves a list of all posts.
     * 
     * @return A list of Post objects representing all posts in the system.
     */
    @GetMapping("/post")
    public List<Post> getAllPost() {
        return this.postService.getAllPosts();
    }

    /**
     * Retrieves posts associated with a specific account.
     * 
     * @param id Unique identifier of the account to fetch posts.
     * @return A list of posts associated with the account identified by 'accountId'.
     */
    @GetMapping("/account/{id}/post")
    public List<Post> getAllPostByAccount(@PathVariable("id") long id) {
        return this.postService.getAllPostsByAccountId(id);
    }

    /**
     * Method to create a new post
     * 
     * Body: 
     * {
     *   "imageUrl": "The image url",
     *   "description": "The post description",
     *   "account": {
     *      "accountId": ID of the account that is posting
     *   }
     * }
     * 
     * @param post The post to be created, provided as JSON in the request body.
     */
    @PostMapping("/post")
    public Post addPost(@RequestBody Post post) {
        return this.postService.addPost(post);
    }

    /**
     * Updates an existing post with new information.
     * 
     * Request Headers:
     * account-name: "Account name"
     * password: "Password"
     * 
     * Body:
     * {
     *   "postId": The post id,
     *   "imageUrl": A new image url (optional),
     *   "description": A new description (optional)
     * }
     * 
     * @param post The post object containing updated information for the post to be modified.
     * @param accountName The accountName that is initiating the request. (Sent via the "account-name" request header)
     * @param password The password of that account. (Sent via the "password" request header)
     */
    @PutMapping("/post")
    public Post updatePost(@RequestBody Post post, @RequestHeader("account-name") String accountName, @RequestHeader("password") String password) {
        return this.postService.updatePost(post, accountName, password);
    }

    /**
     * Deletes a post based on its information.
     * 
     * Request Headers:
     * account-name: "Account name"
     * password: "Password"
     * 
     * Body:
     * {
     *   "postId": The post id
     * }
     * 
     * @param post The post object containing information about the post to be deleted.
     * @param accountName The accountName that is initiating the request. (Sent via the "account-name" request header)
     * @param password The password of that account. (Sent via the "password" request header)
     */
    @DeleteMapping("/post")
    public void deletePost(@RequestBody Post post, @RequestHeader("account-name") String accountName, @RequestHeader("password") String password) {
        this.postService.deletePost(post, accountName, password);
    }
    @PutMapping("/post/{postId}/{numberOfLikes}")
    public void updatePostLikes(@PathVariable("postId") long postId, @PathVariable("numberOfLikes") int numberOfLikes){
        this.postService.updatePostLikes(postId, numberOfLikes);
    }
}
