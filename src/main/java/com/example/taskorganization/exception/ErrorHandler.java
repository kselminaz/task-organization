package com.example.taskorganization.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.example.taskorganization.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception exception) {
        log.error("Exception:", exception);
        return new ErrorResponse(SERVER_ERROR.toString(), "Unexpected server error");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handle(NotFoundException exception) {
        log.error("NotFoundException:", exception);
        return new ErrorResponse(DATA_NOT_FOUND.toString(), exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handle(AuthenticationException exception) {
        log.error("AuthenticationException:", exception);
        return new ErrorResponse(INVALID_USER_LOGIN_CREDENTIALS.toString(), exception.getMessage());
    }

    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handle(UserExistsException exception) {
        log.error("UserExistsException:", exception);
        return new ErrorResponse(USERNAME_MAil_EXISTS.toString(), exception.getMessage());
    }

    @ExceptionHandler(ConfirmPasswordException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handle(ConfirmPasswordException exception) {
        log.error("ConfirmPasswordException:", exception);
        return new ErrorResponse(PASSWORD_NOT_CONFIRMED.toString(), exception.getMessage());
    }

    @ExceptionHandler(UserNotExistsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handle(UserNotExistsException exception) {
        log.error("UserNotExistsException:", exception);
        return new ErrorResponse(USERNAME_OR_MAil_NOT_EXISTS.toString(), exception.getMessage());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handle(IncorrectPasswordException exception) {
        log.error("IncorrectPasswordException:", exception);
        return new ErrorResponse(INCORRECT_PASSWORD.toString(), exception.getMessage());
    }

    @ExceptionHandler(UserDisabledException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handle(UserDisabledException exception) {
        log.error("UserDisabledException:", exception);
        return new ErrorResponse(USER_IS_DISABLED.toString(), exception.getMessage());
    }

    @ExceptionHandler(MailSendException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(MailSendException exception) {
        log.error("MailSendException:", exception);
        return new ErrorResponse(MAIL_SENDING_ERROR.toString(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<ErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ErrorResponse(fieldName, errorMessage));
        });
        return errors;
    }

}
