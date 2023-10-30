package com.example.demo.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class Login {
    private String username;    //Object String 'username' for storing the login username
    private String password;    //Object String 'password' for storing the login password
}
