package com.prography.yakgwa.global.format.exception.vote;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.vote.errorCode.VoteErrorCode.NOT_VALID_VOTEDATE_EXCEPTION;


public class NotValidMeetVoteDateException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_VALID_VOTEDATE_EXCEPTION;

    private NotValidMeetVoteDateException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotValidMeetVoteDateException() {
        this(CODE);
    }
}
