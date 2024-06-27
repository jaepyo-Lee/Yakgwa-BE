package com.prography.yakgwa.global.format.exception.meet;


import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.meet.errorCode.MeetErrorCode.BEFORE_END_VOTE_EXCEPTION;

public class BeforeEndVoteException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = BEFORE_END_VOTE_EXCEPTION;

    private BeforeEndVoteException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public BeforeEndVoteException() {
        this(CODE);
    }
}
