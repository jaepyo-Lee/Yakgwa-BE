package com.prography.yakgwa.global.format.exception.participant;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.participant.errorCode.ParticipantErrorCode.ALREADY_PARTICIPANT_IN_MEET_EXCEPTION;
import static com.prography.yakgwa.global.format.exception.participant.errorCode.ParticipantErrorCode.NOT_FOUND_PARTICIPANT_EXCEPTION;

public class NotFoundParticipantException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_FOUND_PARTICIPANT_EXCEPTION;

    private NotFoundParticipantException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotFoundParticipantException() {
        this(CODE);
    }
}
