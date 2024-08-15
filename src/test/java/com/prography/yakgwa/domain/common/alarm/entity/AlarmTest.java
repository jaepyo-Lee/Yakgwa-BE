package com.prography.yakgwa.domain.common.alarm.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlarmTest {
    @Test
    void 알람전송되었을떄알람전송여부변경() {
        // given
        Alarm alarm = Alarm.builder().send(false).build();

        // when
        System.out.println("=====Logic Start=====");

        alarm.send();

        System.out.println("=====Logic End=====");
        // then
        assertThat(alarm.isSend()).isTrue();
    }

}