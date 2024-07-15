package com.prography.yakgwa.global.format.exception.participant;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.participant.errorCode.ParticipantErrorCode.ALREADY_PARTICIPANT_IN_MEET_EXCEPTION;

public class AlreadyParticipantInMeetException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = ALREADY_PARTICIPANT_IN_MEET_EXCEPTION;

    private AlreadyParticipantInMeetException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public AlreadyParticipantInMeetException() {
        this(CODE);
    }
}
