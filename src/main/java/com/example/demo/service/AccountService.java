package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.Role;
import com.example.demo.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Component
@Transactional
public class AccountService {
    //'AccountRepository' object
    private AccountRepository accountRepository;
    private CommentService commentService;
    private PostService postService;

    @Autowired
    public AccountService(AccountRepository accountRepository, CommentService commentService, PostService postService){
        this.accountRepository = accountRepository;
        this.commentService = commentService;
        this.postService = postService;
    }

    public Account login(Account submittedAccount) {
        Account account = this.getAccountByAccountName(submittedAccount.getAccountName());

        if(account == null || !submittedAccount.getPassword().equals(account.getPassword())) {
            return null;
        }

        return account;
    }

    public Account register(Account submittedAccount) {
        Account account = this.getAccountByAccountName(submittedAccount.getAccountName());

        if(account != null) {
            return null;
        }

        submittedAccount.setDisabled(false);

        return this.saveAccount(submittedAccount);
    }

    //Getting all Accounts
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    /**
     * 
     * @return
     */
    public Account getAccountByAccountName(String accountName) {
        Optional<Account> account = this.accountRepository.findAccountByAccountName(accountName);

        if(!account.isPresent()) {
            return null;
        }

        return account.get();
    }

    /**
     * 
     * @param role
     * @return
     */
    public List<Account> getAccountsByRole(Role role) {
        return null;
    }

    /**
     * 
     * @return
     */
    public List<Account> getAllActiveAccounts() {
        return null;
    }

    /**
     * Saves a new account to the database.
     * 
     * @param account
     */
    public Account saveAccount(Account account){
        return this.accountRepository.save(account);
    }

    /**
     * 
     * @param updatedAccount
     * @param accountName
     * @param password
     * @return
     */
    public Account updateAccount(long id, Account updatedAccount, String accountName, String password) {
        Optional<Account> account = this.accountRepository.findById(id);

        if(!account.isPresent()) {
            return null;
        }

        Optional<Account> accountMakingRequest = this.accountRepository.findAccountByAccountName(accountName);

        if(!accountMakingRequest.isPresent()) {
            return null;
        }

        if(!accountMakingRequest.get().getPassword().equals(password) || accountMakingRequest.get().getRole() != Role.ADMIN) {
            return null;
        }

        Account accountToUpdate = account.get();

        if(updatedAccount.getAccountName() != null) {
            accountToUpdate.setAccountName(updatedAccount.getAccountName());
        }

        if(updatedAccount.getEmail() != null) {
            accountToUpdate.setEmail(updatedAccount.getEmail());
        }

        if(updatedAccount.getFirstName() != null) {
            accountToUpdate.setFirstName(updatedAccount.getFirstName());
        }

        if(updatedAccount.getLastName() != null) {
            accountToUpdate.setLastName(updatedAccount.getLastName());
        }

        if(updatedAccount.getPhoneNumber() != null) {
            accountToUpdate.setPhoneNumber(updatedAccount.getPhoneNumber());
        }

        if(updatedAccount.getRole() != null) {
            accountToUpdate.setRole(updatedAccount.getRole());
        }

        return this.accountRepository.save(accountToUpdate);
    } 

    /**
     * 
     * @param id
     * @param accountName
     * @param password
     * @return
     */
    public Account toggleAccountVisibility(long id, String accountName, String password) {
        Optional<Account> account = this.accountRepository.findById(id);

        if(!account.isPresent()) {
            return null;
        }

        Optional<Account> accountMakingRequest = this.accountRepository.findAccountByAccountName(accountName);

        if(!accountMakingRequest.isPresent()) {
            return null;
        }

        if(!accountMakingRequest.get().getPassword().equals(password) || accountMakingRequest.get().getRole() != Role.ADMIN) {
            return null;
        }

        Account accountToUpdate = account.get();
        accountToUpdate.setDisabled(!accountToUpdate.isDisabled());

        return this.accountRepository.save(accountToUpdate);
    }

    /**
     * 
     * @param id
     * @param accountName
     * @param password
     */
    public boolean deleteAccount(long id, String accountName, String password) {
        Optional<Account> account = this.accountRepository.findById(id);

        if(!account.isPresent()) {
            return false;
        }

        Optional<Account> accountMakingRequest = this.accountRepository.findAccountByAccountName(accountName);

        if(!accountMakingRequest.isPresent()) {
            return false;
        }

        if(!accountMakingRequest.get().getPassword().equals(password) || accountMakingRequest.get().getRole() != Role.ADMIN) {
            return false;
        }

        List<Comment> comments = this.commentService.getAllCommentsByAccountId(id);

        for(Comment comment : comments) {
            this.commentService.deleteComment(comment.getPost().getPostId(), comment, accountName, password);
        }

        List<Post> posts = this.postService.getAllPostsByAccountId(id);

        for(Post post : posts) {
            this.postService.deletePost(post, accountName, password);
        }
        
        this.accountRepository.delete(account.get());
        return true;
    }
}
