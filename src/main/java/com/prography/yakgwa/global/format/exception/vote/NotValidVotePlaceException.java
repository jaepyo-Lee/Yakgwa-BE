package com.prography.yakgwa.global.format.exception.vote;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.vote.errorCode.VoteErrorCode.NOT_VALID_VOTE_PLACE_EXCEPTION;


public class NotValidVotePlaceException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_VALID_VOTE_PLACE_EXCEPTION;

    private NotValidVotePlaceException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotValidVotePlaceException() {
        this(CODE);
    }
}
