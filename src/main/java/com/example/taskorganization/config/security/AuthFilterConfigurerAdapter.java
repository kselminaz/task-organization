package com.example.taskorganization.config.security;

import com.example.taskorganization.service.abstraction.AuthService;
import com.example.taskorganization.service.implementation.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFilterConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthRequestFilter authRequestFilter;

    public void configure(HttpSecurity http) {

        http.addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
