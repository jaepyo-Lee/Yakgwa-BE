package com.prography.yakgwa.global.format.exception.vote;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.vote.errorCode.VoteErrorCode.ALREADY_PLACE_CONFIRM_EXCEPTION;
import static com.prography.yakgwa.global.format.exception.vote.errorCode.VoteErrorCode.NOT_VALID_VOTETIME_EXCEPTION;

public class NotValidVoteTimeException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_VALID_VOTETIME_EXCEPTION;

    private NotValidVoteTimeException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotValidVoteTimeException() {
        this(CODE);
    }
}
