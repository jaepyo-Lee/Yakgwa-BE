package com.prography.yakgwa.global.format.exception.meet;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.meet.errorCode.MeetErrorCode.MEET_TIME_PARAM_DUPLICATION_EXCEPTION;

public class MeetTimeParamDuplicationException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = MEET_TIME_PARAM_DUPLICATION_EXCEPTION;

    private MeetTimeParamDuplicationException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public MeetTimeParamDuplicationException() {
        this(CODE);
    }
}
