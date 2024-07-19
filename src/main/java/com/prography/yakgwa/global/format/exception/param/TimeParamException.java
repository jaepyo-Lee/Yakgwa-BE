package com.prography.yakgwa.global.format.exception.param;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.param.errorCode.ParamErrorCode.TIME_PARAM_EXCEPTION;

public class TimeParamException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = TIME_PARAM_EXCEPTION;

    private TimeParamException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public TimeParamException() {
        this(CODE);
    }
}
