package com.example.demo.entity;

import lombok.*;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.demo.entity.Role;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;

@Entity(name = "account")    //name of my table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;     //long object "accountId" unique identifier

    @Column
    private String accountName; //object string 'accountName'

    @Column
    private String password;    //object string 'password'

    @Column
    private String firstName;   //object string 'firstName'

    @Column
    private String lastName;    //object string 'lastName'

    @Column
    private String email;       //object string 'email'

    @Column
    private String phoneNumber; //object string 'phoneNumber'

    //depending on what the user selects it will establish functionality of the different accounts
    @Column
    private Role role;

    @Column
    private boolean isDisabled; //object boolean 'isDisabled' true/false

    @OneToMany(cascade = CascadeType.ALL)  //one-to-many relationship with the 'Post' entity.
    @JsonIgnore //commands JSON serialization to ignore the field when converting to JSON
    private List<Post> posts;

    @OneToMany(cascade = CascadeType.ALL)  //one-to-many relationship with the 'Comment' entity.
    @JsonIgnore //commands JSON serialization to ignore the field when converting to JSON
    private List<Comment> comments;
}
