package com.prography.yakgwa.global.format.exception.slot;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.slot.errorCode.SlotErrorCode.NOT_MATCH_SLOT_IN_MEET_EXCEPTION;

public class NotMatchSlotInMeetException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_MATCH_SLOT_IN_MEET_EXCEPTION;

    private NotMatchSlotInMeetException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotMatchSlotInMeetException() {
        this(CODE);
    }
}
