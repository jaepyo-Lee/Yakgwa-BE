package com.prography.yakgwa.global.format.exception.meet;


import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.meet.errorCode.MeetErrorCode.NOT_FOUND_MEET_EXCEPTION;

public class NotFoundMeetException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_FOUND_MEET_EXCEPTION;

    private NotFoundMeetException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotFoundMeetException() {
        this(CODE);
    }
}
