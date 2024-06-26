package com.prography.yakgwa.global.format.exception.user.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorEnumCode {
    NOT_FOUND_USER_EXCEPTION("UE001","해당 사용자는 존재하지 않는 사용자입니다.");

    private final String code;
    private final String message;
}
