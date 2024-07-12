package com.prography.yakgwa.global.format.exception.meet;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.meet.errorCode.MeetErrorCode.CONFIRM_PLACE_COUNT_EXCEPTION;

public class ConfirmPlaceCountException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = CONFIRM_PLACE_COUNT_EXCEPTION;

    private ConfirmPlaceCountException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public ConfirmPlaceCountException() {
        this(CODE);
    }
}
