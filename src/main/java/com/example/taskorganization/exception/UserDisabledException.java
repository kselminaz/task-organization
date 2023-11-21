package com.example.taskorganization.exception;

public class UserDisabledException extends RuntimeException{

    public UserDisabledException(String message){
        super(message);
    }
}
