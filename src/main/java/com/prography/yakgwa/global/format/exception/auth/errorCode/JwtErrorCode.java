package com.prography.yakgwa.global.format.exception.auth.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JwtErrorCode implements ErrorEnumCode {
    EXPIRED_JWT_TOKEN_ERROR("JE001","만료된 JWT토큰입니다."),
    INVALID_REFRESH_TOKEN("JE002","올바르지 않는 refresh token입니다."),
    BLACKLIST_REFRESH_TOKEN("JE003","로그아웃된 refresh token입니다.");

    private final String code;
    private final String message;
}
