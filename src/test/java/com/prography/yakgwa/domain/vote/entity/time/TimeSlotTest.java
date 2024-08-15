package com.prography.yakgwa.domain.vote.entity.time;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {
    @Test
    void 시간후보확정() {
        // given
        TimeSlot timeSlot = TimeSlot.builder().confirm(false).build();

        // when
        System.out.println("=====Logic Start=====");

        timeSlot.confirm();

        System.out.println("=====Logic End=====");
        // then
        assertThat(timeSlot.getConfirm()).isTrue();
    }

    @Test
    void 시간후보확정여부조회테스트() {
        // given
        TimeSlot timeSlot1 = TimeSlot.builder().confirm(false).build();
        TimeSlot timeSlot2 = TimeSlot.builder().confirm(true).build();
        // when
        System.out.println("=====Logic Start=====");
        System.out.println("=====Logic End=====");
        // then
        assertAll(()->assertThat(timeSlot1.isConfirm()).isFalse(),
                ()-> assertThat(timeSlot2.isConfirm()).isTrue());
    }
    @Test
    void 시간후보동일성테스트() {
        // given
        TimeSlot timeSlot1 = TimeSlot.builder().id(1L).build();
        TimeSlot timeSlot2 = TimeSlot.builder().id(1L).build();

        // when
        System.out.println("=====Logic Start=====");

        boolean equals = timeSlot1.equals(timeSlot2);

        System.out.println("=====Logic End=====");
        // then
        assertThat(equals).isTrue();

    }
}