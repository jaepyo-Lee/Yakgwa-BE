package com.prography.yakgwa.global.format.exception.param.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeetErrorCode implements ErrorEnumCode {
    TIME_PARAM_EXCEPTION("PE001", "모임 확정시간과 투표기간은 동시에 입력 또는 제외될수없습니다. 파라미터를 확인해주세요."),
    MULTIPART_PARAMETER_EXCEPTION("PE002","사진 파라미터를 확인해주세요.");
    private final String code;
    private final String message;
}
