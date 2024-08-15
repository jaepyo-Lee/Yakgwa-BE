package com.prography.yakgwa.global.format.exception.vote;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.vote.errorCode.VoteErrorCode.NOT_VALID_CONFIRM_TIME_EXCEPTION;


public class NotValidConfirmTimeException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_VALID_CONFIRM_TIME_EXCEPTION;

    private NotValidConfirmTimeException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotValidConfirmTimeException() {
        this(CODE);
    }
}
