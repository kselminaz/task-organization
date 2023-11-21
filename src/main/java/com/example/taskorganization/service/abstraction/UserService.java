package com.example.taskorganization.service.abstraction;

import com.example.taskorganization.model.request.ChangePasswordRequest;
import com.example.taskorganization.model.request.SignInRequest;
import com.example.taskorganization.model.request.SignUpRequest;
import com.example.taskorganization.model.response.SignInResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    void signUp(SignUpRequest signUpRequest, HttpServletRequest request);

    void checkConfirmationCode(String code);

    SignInResponse signIn(SignInRequest request);

    void forgotPassword(String username, HttpServletRequest request);

    void checkChangePassword(String code);

    void changePassword(ChangePasswordRequest request);



}
