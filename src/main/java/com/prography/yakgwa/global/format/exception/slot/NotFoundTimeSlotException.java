package com.prography.yakgwa.global.format.exception.slot;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.slot.errorCode.SlotErrorCode.NOT_FOUND_TIMESLOT_EXCEPTION;

public class NotFoundTimeSlotException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_FOUND_TIMESLOT_EXCEPTION;

    private NotFoundTimeSlotException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotFoundTimeSlotException() {
        this(CODE);
    }
}
