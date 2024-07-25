package com.prography.yakgwa.global.format.exception.slot.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlotErrorCode implements ErrorEnumCode {
    NOT_FOUND_PLACESLOT_EXCEPTION("SE001","추가되지 않은 모임장소후보입니다.."),
    NOT_FOUND_TIMESLOT_EXCEPTION("SE002","추가되지 않은 모임시간후보입니다."),
    ALREADY_APPEND_PLACE_EXCEPTION("S3003","이미 추가되어있는 후보지입니다."),
    NOT_MATCH_SLOT_IN_MEET_EXCEPTION("SE004","모임에 없는 장소후보입니다.");

    private final String code;
    private final String message;
}
