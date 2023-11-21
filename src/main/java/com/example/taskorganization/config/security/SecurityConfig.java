package com.example.taskorganization.config.security;

import com.example.taskorganization.dao.repository.UserRepository;
import com.example.taskorganization.service.abstraction.AuthService;
import com.example.taskorganization.service.implementation.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final AuthRequestFilter authRequestFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        //configurations
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

        //authorizes
        http.authorizeHttpRequests(auth -> auth.requestMatchers("v1/user/sign-in").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("v1/user/register").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("v1/user/confirm-mail").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("v1/user/forgot-password").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("v1/user/change-password").permitAll());

        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());

        http.apply(new AuthFilterConfigurerAdapter(authRequestFilter));
        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(new UserDetailsServiceImpl(userRepository));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
