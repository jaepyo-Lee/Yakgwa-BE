package com.prography.yakgwa.global.format.exception.vote.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteErrorCode implements ErrorEnumCode {
    ALREADY_PLACE_CONFIRM_VOTE_EXCEPTION("VE001","이미 장소가 확정된 투표이기에 투표가 불가합니다."),
    ALREADY_TIME_CONFIRM_VOTE_EXCEPTION("VE002","이미 시간이 확정된 투표이기에 투표가 불가합니다."),
    PARTICIPANT_CONFIRM_EXCEPTION("VE003","약과장만이 확정권한이 있습니다.");

    private final String code;
    private final String message;
}
