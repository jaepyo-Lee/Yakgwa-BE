package com.prography.yakgwa.global.format.exception.user;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.user.errorCode.UserErrorCode.NOT_MATCH_ADMIN_ROLE_EXCEPTION;

public class NotMatchAdminRoleException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_MATCH_ADMIN_ROLE_EXCEPTION;


    private NotMatchAdminRoleException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotMatchAdminRoleException() {
        this(CODE);
    }
}
