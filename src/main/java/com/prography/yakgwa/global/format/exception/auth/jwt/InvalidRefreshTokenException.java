package com.prography.yakgwa.global.format.exception.auth.jwt;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.auth.errorCode.JwtErrorCode.INVALID_REFRESH_TOKEN;

public class InvalidRefreshTokenException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = INVALID_REFRESH_TOKEN;

    public InvalidRefreshTokenException(){
        this(CODE);
    }
    private InvalidRefreshTokenException(ErrorEnumCode errorEnumCode) {
        super(errorEnumCode);
    }
}
