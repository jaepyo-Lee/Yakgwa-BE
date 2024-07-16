package com.prography.yakgwa.global.format.exception.magazine.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MagazineErrorCode implements ErrorEnumCode {
    NOT_FOUND_MAGAZINE_EXCEPTION("MAE001","존재하지 않는 매거진입니다. 다시 확인해주세요.");
    private final String code;
    private final String message;
}
