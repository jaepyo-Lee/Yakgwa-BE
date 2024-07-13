package com.prography.yakgwa.global.format.exception.participant;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.meet.errorCode.MeetErrorCode.NOT_FOUND_THEME_EXCEPTION;
import static com.prography.yakgwa.global.format.exception.participant.errorCode.MeetErrorCode.ALREADY_PARTICIPANT_IN_MEET_EXCEPTION;

public class AlreadyParticipantInMeetException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = ALREADY_PARTICIPANT_IN_MEET_EXCEPTION;

    private AlreadyParticipantInMeetException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public AlreadyParticipantInMeetException() {
        this(CODE);
    }
}
