package com.prography.yakgwa.global.format.exception.user;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.user.errorCode.UserErrorCode.NOT_FOUND_USER_EXCEPTION;

public class NotFoundUserException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_FOUND_USER_EXCEPTION;


    private NotFoundUserException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotFoundUserException() {
        this(CODE);
    }
}
