package com.prography.yakgwa.global.format.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlarmType {
    END_VOTE("투표마감!","모임의 투표가 마감되었습니다! 확인해주세요!"),PROMISE_DAY("약속 당일","모임의 약속당일입니다!");
    private final String title;
    private final String body;
}
