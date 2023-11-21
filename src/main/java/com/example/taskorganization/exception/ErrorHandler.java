package com.example.taskorganization.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.example.taskorganization.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception exception) {
        log.error("Exception: { }", exception);
        return new ErrorResponse(SERVER_ERROR, "Unexpected server error");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handle(NotFoundException exception) {
        log.error("NotFoundException: { } ", exception);
        return new ErrorResponse(DATA_NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handle(AuthenticationException exception) {
        log.error("AuthenticationException: { } ", exception);
        return new ErrorResponse(INVALID_USER_LOGIN_CREDENTIALS, exception.getMessage());
    }

    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handle(UserExistsException exception) {
        log.error("UserExistsException: { } ", exception);
        return new ErrorResponse(USERNAME_MAil_EXISTS, exception.getMessage());
    }

    @ExceptionHandler(ConfirmPasswordException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handle(ConfirmPasswordException exception) {
        log.error("ConfirmPasswordException: { } ", exception);
        return new ErrorResponse(PASSWORD_NOT_CONFIRMED, exception.getMessage());
    }

    @ExceptionHandler(UserNotExistsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handle(UserNotExistsException exception) {
        log.error("UserNotExistsException: { }", exception);
        return new ErrorResponse(USERNAME_OR_MAil_NOT_EXISTS, exception.getMessage());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handle(IncorrectPasswordException exception) {
        log.error("IncorrectPasswordException: { }", exception);
        return new ErrorResponse(INCORRECT_PASSWORD, exception.getMessage());
    }

    @ExceptionHandler(UserDisabledException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handle(UserDisabledException exception) {
        log.error("UserDisabledException: { }", exception);
        return new ErrorResponse(USER_IS_DISABLED, exception.getMessage());
    }

    @ExceptionHandler(MailSendException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(MailSendException exception) {
        log.error("MailSendException: { }", exception);
        return new ErrorResponse(MAIL_SENDING_ERROR, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
