package com.prography.yakgwa.global.format.exception.meet;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.meet.errorCode.MeetErrorCode.ALREADY_CONFIRM_MEET_EXCEPTION;

public class AlreadyConfirmMeetException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = ALREADY_CONFIRM_MEET_EXCEPTION;

    private AlreadyConfirmMeetException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public AlreadyConfirmMeetException() {
        this(CODE);
    }
}
