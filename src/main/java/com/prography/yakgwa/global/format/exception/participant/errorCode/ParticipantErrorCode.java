package com.prography.yakgwa.global.format.exception.participant.errorCode;

import com.prography.yakgwa.global.format.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParticipantErrorCode implements ErrorEnumCode {
    ALREADY_PARTICIPANT_IN_MEET_EXCEPTION("PE001","이미 참여중인 사용자입니다!"),
    NOT_FOUND_PARTICIPANT_EXCEPTION("PE002","참여하지않고 있는 사용자 또는 모임입니다!");
    private final String code;
    private final String message;
}
