package com.example.taskorganization.exception;

public class UserNotExistsException extends RuntimeException{

    public UserNotExistsException(String message){
        super(message);
    }
}
