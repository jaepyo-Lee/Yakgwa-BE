package com.prography.yakgwa.global.format.exception.magazine;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.magazine.errorCode.MagazineErrorCode.NOT_FOUND_MAGAZINE_EXCEPTION;

public class NotFoundMagazineException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_FOUND_MAGAZINE_EXCEPTION;

    private NotFoundMagazineException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotFoundMagazineException() {
        this(CODE);
    }
}
