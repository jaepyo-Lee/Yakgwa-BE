package com.prography.yakgwa.global.format.exception.meet;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.meet.errorCode.MeetErrorCode.NOT_FOUND_THEME_EXCEPTION;

public class NotFoundThemeException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_FOUND_THEME_EXCEPTION;

    private NotFoundThemeException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotFoundThemeException() {
        this(CODE);
    }
}
