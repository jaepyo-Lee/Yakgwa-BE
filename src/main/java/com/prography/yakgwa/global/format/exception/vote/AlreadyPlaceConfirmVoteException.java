package com.prography.yakgwa.global.format.exception.vote;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.vote.errorCode.VoteErrorCode.ALREADY_PLACE_CONFIRM_VOTE_EXCEPTION;


public class AlreadyPlaceConfirmVoteException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = ALREADY_PLACE_CONFIRM_VOTE_EXCEPTION;

    private AlreadyPlaceConfirmVoteException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public AlreadyPlaceConfirmVoteException() {
        this(CODE);
    }
}
