package com.example.taskorganization.mapper;

import com.example.taskorganization.dao.entity.ChangePasswordEntity;
import com.example.taskorganization.dao.entity.UserEntity;
import com.example.taskorganization.model.dto.MailDto;
import com.example.taskorganization.model.request.SignUpRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserMapper {

    public static UserEntity buildUserEntity(SignUpRequest request){

        return UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .confirmCode(UUID.randomUUID().toString())
                .isEnabled(false)
                .build();
    }
    public static MailDto buildConfirmMailDto(UserEntity user,String siteUrl){
        return MailDto.builder()
                .toAddress(user.getEmail())
                .subject("Confirm your mail")
                .content("Dear "+ user.getUsername()+
                        ",<br> Please click the link below to confirm your registration:<br>" +
                        "<h3><a href="+ siteUrl + "/v1/user/confirm-mail?code=" + user.getConfirmCode()+" target='_self'>Confirm</a></h3>" +
                        "Thank you<br>")
                .build();
    }
    public static MailDto buildResetPasswordMailDto(String email,String code,String siteUrl){

        return MailDto.builder()
                .toAddress(email)
                .subject("Reset your password")
                .content("Please click the link below to reset your password:<br>" +
                        "<h3><a href="+ siteUrl + "/v1/user/change-password?code=" + code+" target='_self'>Reset</a></h3>" +
                        "Thank you<br>")
                .build();
    }
    public static ChangePasswordEntity buildChangePasswordEntity(Long userId){

         return ChangePasswordEntity.builder()
                 .userId(userId)
                 .code(UUID.randomUUID().toString())
                 .expirationTime(LocalDateTime.now().plusMinutes(30))
                 .build();
    }
}
