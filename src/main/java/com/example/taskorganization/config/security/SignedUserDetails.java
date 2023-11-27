package com.example.taskorganization.config.security;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignedUserDetails {

    private Long userId;
    private String username;
    private String email;

}
