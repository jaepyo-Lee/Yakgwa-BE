package com.prography.yakgwa.global.format.exception.auth;


import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.auth.errorCode.AuthErrorCode.NOT_ENOUGH_WEBCLIENT_LOGIN_PARAM;

public class NotEnoughWebclientLoginParamException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_ENOUGH_WEBCLIENT_LOGIN_PARAM;


    private NotEnoughWebclientLoginParamException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotEnoughWebclientLoginParamException() {
        this(CODE);
    }
}
