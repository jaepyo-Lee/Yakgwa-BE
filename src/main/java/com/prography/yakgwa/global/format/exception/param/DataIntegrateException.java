package com.prography.yakgwa.global.format.exception.param;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.param.errorCode.ParamErrorCode.DATA_INTEGRATE_EXCEPTION;
import static com.prography.yakgwa.global.format.exception.param.errorCode.ParamErrorCode.MULTIPART_PARAMETER_EXCEPTION;

public class DataIntegrateException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = DATA_INTEGRATE_EXCEPTION;

    private DataIntegrateException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public DataIntegrateException() {
        this(CODE);
    }
}
