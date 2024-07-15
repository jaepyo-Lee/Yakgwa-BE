package com.prography.yakgwa.global.format.exception.vote;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.vote.errorCode.VoteErrorCode.ALREADY_TIME_CONFIRM_VOTE_EXCEPTION;


public class AlreadyTimeConfirmVoteException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = ALREADY_TIME_CONFIRM_VOTE_EXCEPTION;

    private AlreadyTimeConfirmVoteException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public AlreadyTimeConfirmVoteException() {
        this(CODE);
    }
}
