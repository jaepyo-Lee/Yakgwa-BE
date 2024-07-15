package com.prography.yakgwa.global.format.exception.user.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorEnumCode {
    NOT_FOUND_USER_EXCEPTION("UE001","해당 사용자는 존재하지 않는 사용자입니다."),
    NOT_MATCH_ADMIN_ROLE_EXCEPTION("UE002","관리자만 가능한 기능입니다. 권한을 확인해주세요");

    private final String code;
    private final String message;
}
