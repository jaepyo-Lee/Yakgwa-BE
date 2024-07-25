package com.prography.yakgwa.global.format.exception.place.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlaceErrorCode implements ErrorEnumCode {
    NOT_FOUND_PLACE_EXCEPTION("SE001","해당 장소를 찾을수 없습니다.");

    private final String code;
    private final String message;
}
