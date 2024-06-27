package com.prography.yakgwa.global.format.exception.meet;


import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.meet.errorCode.MeetErrorCode.NOT_DEFINE_TIME_VOTE_EXCEPTION;

public class NotDefineTimeVoteException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_DEFINE_TIME_VOTE_EXCEPTION;

    private NotDefineTimeVoteException (ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotDefineTimeVoteException () {
        this(CODE);
    }
}
