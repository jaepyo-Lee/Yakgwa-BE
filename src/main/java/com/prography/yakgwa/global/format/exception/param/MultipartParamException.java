package com.prography.yakgwa.global.format.exception.param;

import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.ErrorEnumCode;

import static com.prography.yakgwa.global.format.exception.param.errorCode.ParamErrorCode.MULTIPART_PARAMETER_EXCEPTION;

public class MultipartParamException extends ApplicationRunException {
    private static final ErrorEnumCode CODE = MULTIPART_PARAMETER_EXCEPTION;

    private MultipartParamException(ErrorEnumCode CODE) {
        super(CODE);
    }

    public MultipartParamException() {
        this(CODE);
    }
}
