package com.prography.yakgwa.global.format.exception.place;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.place.errorCode.PlaceErrorCode.NOT_FOUND_PLACE_EXCEPTION;
import static com.prography.yakgwa.global.format.exception.slot.errorCode.SlotErrorCode.ALREADY_APPEND_PLACE_EXCEPTION;


public class NotFoundPlaceException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = NOT_FOUND_PLACE_EXCEPTION;

    private NotFoundPlaceException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public NotFoundPlaceException() {
        this(CODE);
    }
}
