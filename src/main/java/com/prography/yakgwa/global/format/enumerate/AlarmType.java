package com.prography.yakgwa.global.format.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlarmType {
    END_VOTE("투표마감!","의 모임정보를 확인해주세요!"),PROMISE_DAY("약속 당일","의 약속당일입니다!");
    private final String title;
    private final String body;
    public String makeBodyMessage(String meetTitle){
        return meetTitle + this.getBody();
    }
}
