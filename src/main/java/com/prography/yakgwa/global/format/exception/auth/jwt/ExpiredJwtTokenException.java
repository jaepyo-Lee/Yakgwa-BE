package com.prography.yakgwa.global.format.exception.auth.jwt;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.Getter;

import static com.prography.yakgwa.global.format.exception.auth.errorCode.JwtErrorCode.EXPIRED_JWT_TOKEN_ERROR;

@Getter
public class ExpiredJwtTokenException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = EXPIRED_JWT_TOKEN_ERROR;

    public ExpiredJwtTokenException(){
        this(CODE);
    }
    private ExpiredJwtTokenException(ErrorEnumCode errorEnumCode) {
        super(errorEnumCode);
    }
}
