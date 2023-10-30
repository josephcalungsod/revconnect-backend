package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = {"http://localhost:3000", "http://revconnect.azurewebsites.net"})
@RestController
public class AccountController {
    //Instantiating 'accountService' object
    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    /**
     * Retrieves all of the accounts in the database.
     * 
     * @return A list of the accounts.
     */
    @GetMapping("/account")
    public List<Account> getAccounts() {
        return accountService.getAllAccounts();
    }

    /**
     * 
     * 
     * Body:
     * {
     *    "accountName": "The accounts name",
     *    "password": "The accounts password"
     * }
     * 
     * @param submittedAccount
     * @return
     */
    @PostMapping("/account/login")
    public ResponseEntity<Account> login(@RequestBody Account submittedAccount) {
        Account account = this.accountService.login(submittedAccount);
        HttpStatus status = HttpStatus.OK;

        if(account == null) {
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<Account>(account, status);
    }

    /**
     * 
     * 
     * Currently the minimum requirements to register an account is the account
     * name and the password. 
     * 
     * Body:
     * {
     *    "accountName": "The accounts name",
     *    "password": "The accounts password",
     *    "firstName": "Some first name",
     *    "lastName": "Some last name",
     *    "email": "Some email",
     *    "phoneNumber": "Some phone number",
     *    "role": "Some role",
     * }
     * 
     * @param submittedAccount The account to be registered.
     * @return
     */
    @PostMapping("/account/register")
    public ResponseEntity<Account> register(@RequestBody Account submittedAccount) {
        Account account = this.accountService.register(submittedAccount);
        HttpStatus status = HttpStatus.CREATED;

        if(account == null) {
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Account>(account, status);
    }

    /**
     * 
     * @param account
     * @param accountName
     * @param password
     * @return
     */
    @PutMapping("/account/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") long id, @RequestBody Account account, @RequestHeader("account-name") String accountName, @RequestHeader("password") String password) {
        Account updatedAccount = this.accountService.updateAccount(id, account, accountName, password);
        HttpStatus status = HttpStatus.ACCEPTED;

        if(updatedAccount == null) {
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Account>(updatedAccount, status);
    }

    @PutMapping("/account/{id}/disable")
    public ResponseEntity<Account> disableAccount(@PathVariable("id") long id, @RequestHeader("account-name") String accountName, @RequestHeader("password") String password) {
        Account updatedAccount = this.accountService.toggleAccountVisibility(id, accountName, password);
        HttpStatus status = HttpStatus.ACCEPTED;

        if(updatedAccount == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        
        return new ResponseEntity<Account>(updatedAccount, status);
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") long id, @RequestHeader("account-name") String accountName, @RequestHeader("password") String password) {
        HttpStatus status = HttpStatus.ACCEPTED;

        if(!this.accountService.deleteAccount(id, accountName, password)) {
            status = HttpStatus.BAD_REQUEST;
        }
        
        return new ResponseEntity<>(null, status);
    }
}
