package com.prography.yakgwa.global.format.exception.auth;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.auth.errorCode.AuthErrorCode.NOT_SUPPORT_LOGIN_TYPE_EXCEPTION;

public class NotSupportLoginTypeException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_SUPPORT_LOGIN_TYPE_EXCEPTION;

    public NotSupportLoginTypeException(ErrorEnumCode errorEnumCode) {
        super(errorEnumCode);
    }

    public NotSupportLoginTypeException() {
        super(CODE);
    }
}
