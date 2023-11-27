package com.example.taskorganization.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    SERVER_ERROR,
    DATA_NOT_FOUND,
    USERNAME_MAil_EXISTS,
    PASSWORD_NOT_CONFIRMED,
    MAIL_SENDING_ERROR,

    USERNAME_OR_MAil_NOT_EXISTS,

    USER_IS_DISABLED,

    INVALID_USER_LOGIN_CREDENTIALS,

    INCORRECT_PASSWORD

}
