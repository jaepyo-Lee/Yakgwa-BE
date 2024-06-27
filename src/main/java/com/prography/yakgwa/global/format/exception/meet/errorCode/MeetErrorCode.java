package com.prography.yakgwa.global.format.exception.meet.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeetErrorCode implements ErrorEnumCode {
    NOT_FOUND_MEET_EXCEPTION("ME001","존재하지 않는 모임입니다."),
    NOT_DEFINE_TIME_VOTE_EXCEPTION("ME002","모임의 투표시간 범위가 정의되어있지 않습니다."),
    ALREADY_CONFIRM_MEET_EXCEPTION("ME003","이미 모임이 확정된 모임입니다. 상태를 다시 확인해주세요"),
    BEFORE_END_VOTE_EXCEPTION("ME004","아직 투표가 끝나지 않았습니다.");
    private final String code;
    private final String message;
}
