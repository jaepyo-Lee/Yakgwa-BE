package com.prography.yakgwa.global.format.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlarmType {
    VOTE_CONFIRM(" 모임","의 약속이 확정되었습니다!"),PROMISE_DAY("약속 당일","모임의 약속당일입니다!");
    private final String title;
    private final String body;
}
