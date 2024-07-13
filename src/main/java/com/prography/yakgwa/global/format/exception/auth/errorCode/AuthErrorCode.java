package com.prography.yakgwa.global.format.exception.auth.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorEnumCode {
    INVALID_LOGIN_PARAM("AE001", "로그인 정보가 틀렸습니다. 아이디와 비밀번호를 다시 확인해주세요"),
    NOT_ENOUGH_WEBCLIENT_LOGIN_PARAM("AE002", "API요청시 파라미터를 확인해주세요. 관리자에게 문의바랍니다."),
    NOT_SUPPORT_LOGIN_TYPE_EXCEPTION("AE003", "지원하지 않는 로그인 타입입니다. 라미터를 확인해주세요");

    private final String code;
    private final String message;
}
