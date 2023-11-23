package com.example.taskorganization.service.implementation;

import com.example.taskorganization.annotation.Log;
import com.example.taskorganization.dao.entity.ChangePasswordEntity;
import com.example.taskorganization.dao.repository.ChangePasswordRepository;
import com.example.taskorganization.exception.*;
import com.example.taskorganization.model.dto.MailDto;
import com.example.taskorganization.model.request.ChangePasswordRequest;
import com.example.taskorganization.util.JwtUtil;
import com.example.taskorganization.dao.repository.UserRepository;
import com.example.taskorganization.mapper.UserMapper;
import com.example.taskorganization.model.request.SignInRequest;
import com.example.taskorganization.model.request.SignUpRequest;
import com.example.taskorganization.model.response.SignInResponse;
import com.example.taskorganization.service.abstraction.UserService;
import com.example.taskorganization.util.MailSenderUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.taskorganization.mapper.UserMapper.buildChangePasswordEntity;
import static lombok.AccessLevel.PRIVATE;

@Service
@Log
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtUtil jwtUtil;
    MailSenderUtil mailSenderUtil;

    ChangePasswordRepository changePasswordRepository;

    @Override
    @Transactional
    public void signUp(SignUpRequest signUpRequest,HttpServletRequest request) {

        var user = userRepository.findByUsernameOrEmail(signUpRequest.getUsername(), signUpRequest.getEmail());

        if (user.isPresent()) throw new UserExistsException("User with username or email already exists");

        if(!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword()))
            throw  new ConfirmPasswordException("Password not confirmed");

        var entity= UserMapper.buildUserEntity(signUpRequest);
        entity.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        try {
            mailSenderUtil.sendMail(UserMapper.buildConfirmMailDto(entity,getSiteURL(request)));
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailSendException("Error in sending mail");
        }

        userRepository.save(entity);


    }
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @Override
    public void checkConfirmationCode(String code) {

        var user= userRepository.findByConfirmCode(code).orElseThrow(() -> new NotFoundException(String.format(
                "User with this confirm code [%s] was not found!", code
        )));
        user.setIsEnabled(true);
        userRepository.save(user);

    }

    @Override
    public SignInResponse signIn(SignInRequest request) {

        var user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername()).
                orElseThrow(() ->new UserNotExistsException("User with username or email not exists"));

        if(!user.isEnabled()) throw new UserDisabledException("User mail is not confirmed");

        var auth=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        return jwtUtil.generateTokens(user);

    }

    @Override
    @Transactional
    public void forgotPassword(String username,HttpServletRequest request) {

        var user = userRepository.findByUsernameOrEmail(username, username).
                orElseThrow(() ->new UserNotExistsException("User with username or email not exists"));

        if(!user.isEnabled()) throw new UserDisabledException("User mail is not confirmed");

       var changePasswordEntity=buildChangePasswordEntity(user.getId());

       changePasswordRepository.save(changePasswordEntity);

        try {
            mailSenderUtil.sendMail(UserMapper.buildResetPasswordMailDto(user.getEmail(), changePasswordEntity.getCode(), getSiteURL(request)));
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailSendException("Error in sending mail");
        }

    }
    @Override
    public void checkChangePassword(String code){

        var changePasswordEntity=changePasswordRepository.findByCodeAndExpirationTimeAfter(code, LocalDateTime.now()).orElseThrow(() -> new NotFoundException(String.format(
                "User with this reset password code [%s] was not found or code time is expired", code
        )));
    }
    @Override
    public void changePassword(ChangePasswordRequest request) {

        var changePasswordEntity=changePasswordRepository.findByCodeAndExpirationTimeAfter(request.getCode(), LocalDateTime.now()).orElseThrow(() -> new NotFoundException(String.format(
                "User with this reset password code [%s] was not found or code time is expired", request.getCode()
        )));

        if(!request.getPassword().equals(request.getConfirmPassword()))
            throw  new ConfirmPasswordException("Password not confirmed");

        var user = userRepository.findById(changePasswordEntity.getUserId()).
                orElseThrow(() ->new UserNotExistsException("User with this reset code not exists"));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        changePasswordRepository.delete(changePasswordEntity);

    }
}
