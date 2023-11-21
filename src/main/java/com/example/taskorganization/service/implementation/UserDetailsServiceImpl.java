package com.example.taskorganization.service.implementation;

import com.example.taskorganization.dao.entity.UserEntity;
import com.example.taskorganization.dao.repository.UserRepository;
import com.example.taskorganization.exception.UserExistsException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UserExistsException("User with username or email already exists"));
    }

    public UserEntity signedUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserExistsException("User with username or email already exists"));
    }

}
