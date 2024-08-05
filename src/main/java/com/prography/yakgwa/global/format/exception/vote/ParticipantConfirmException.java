package com.prography.yakgwa.global.format.exception.vote;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.vote.errorCode.VoteErrorCode.PARTICIPANT_CONFIRM_EXCEPTION;


public class ParticipantConfirmException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = PARTICIPANT_CONFIRM_EXCEPTION;

    private ParticipantConfirmException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public ParticipantConfirmException() {
        this(CODE);
    }
}
