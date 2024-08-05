package com.prography.yakgwa.global.format.exception.vote;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.vote.errorCode.VoteErrorCode.ALREADY_PLACE_CONFIRM_EXCEPTION;

public class AlreadyPlaceConfirmException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = ALREADY_PLACE_CONFIRM_EXCEPTION;

    private AlreadyPlaceConfirmException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public AlreadyPlaceConfirmException() {
        this(CODE);
    }
}
