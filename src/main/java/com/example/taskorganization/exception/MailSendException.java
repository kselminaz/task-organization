package com.example.taskorganization.exception;

public class MailSendException extends RuntimeException{

    public MailSendException(String message){
        super(message);
    }
}
