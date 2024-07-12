package com.prography.yakgwa.global.format.exception.slot;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.slot.errorCode.SlotErrorCode.NOT_FOUND_PLACESLOT_EXCEPTION;


public class NotFoundPlaceSlotException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_FOUND_PLACESLOT_EXCEPTION;

    private NotFoundPlaceSlotException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotFoundPlaceSlotException() {
        this(CODE);
    }
}
