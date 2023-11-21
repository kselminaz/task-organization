package com.example.taskorganization.controller;

import com.example.taskorganization.model.request.ChangePasswordRequest;
import com.example.taskorganization.model.request.SignInRequest;
import com.example.taskorganization.model.request.SignUpRequest;
import com.example.taskorganization.model.response.SignInResponse;
import com.example.taskorganization.service.abstraction.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public void registerUser(@Valid @RequestBody SignUpRequest signUpRequest,HttpServletRequest request) {
        userService.signUp(signUpRequest,request);
    }
    @GetMapping("/confirm-mail")
    public void confirmMail(String code){
        userService.checkConfirmationCode(code);
    }
    @PostMapping("/sign-in")
    public SignInResponse signInUser(@Valid @RequestBody SignInRequest signInRequest) {

        return userService.signIn(signInRequest);
    }
    @GetMapping ("/forgot-password")
    public void forgotPassword(@NotBlank String username,HttpServletRequest request){
        userService.forgotPassword(username,request);
    }
    @GetMapping("/change-password")
    public void changePassword(@NotBlank String code){
        userService.checkChangePassword(code);
    }
    @PostMapping("/change-password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request){
        userService.changePassword(request);
    }


}
