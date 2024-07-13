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
    BEFORE_END_VOTE_EXCEPTION("ME004","아직 투표가 끝나지 않았습니다."),
    MEET_TIME_PARAM_DUPLICATION_EXCEPTION("ME005", "시간은 투표 또는 확정중 한가지만 가능합니다. 파라미터를 확인해주세요."),
    CONFIRM_PLACE_COUNT_EXCEPTION("ME006","확정된 장소는 1개이어야 합니다. 장소파라미터를 다시 확인해주세요."),
    NOT_FOUND_THEME_EXCEPTION("ME007", "존재하지 않는 모임의 테마입니다. 요청을 확인해주세요"),;
    private final String code;
    private final String message;
}
