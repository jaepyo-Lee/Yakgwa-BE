package com.prography.yakgwa.global.format.exception.slot;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.slot.errorCode.SlotErrorCode.ALREADY_APPEND_PLACE_EXCEPTION;
import static com.prography.yakgwa.global.format.exception.slot.errorCode.SlotErrorCode.NOT_FOUND_PLACESLOT_EXCEPTION;


public class AlreadyAppendPlaceException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = ALREADY_APPEND_PLACE_EXCEPTION;

    private AlreadyAppendPlaceException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public AlreadyAppendPlaceException() {
        this(CODE);
    }
}
