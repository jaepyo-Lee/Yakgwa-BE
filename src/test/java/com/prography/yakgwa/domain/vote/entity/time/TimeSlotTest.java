package com.prography.yakgwa.domain.vote.entity.time;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {
    @Test
    void 시간후보확정() {
        // given
        TimeSlot timeSlot = TimeSlot.builder().isConfirm(false).build();

        // when
        System.out.println("=====Logic Start=====");

        timeSlot.confirm();

        System.out.println("=====Logic End=====");
        // then
        assertThat(timeSlot.isConfirm()).isTrue();
    }

    @Test
    void 시간후보확정여부조회테스트() {
        // given
        TimeSlot timeSlot1 = TimeSlot.builder().isConfirm(false).build();
        TimeSlot timeSlot2 = TimeSlot.builder().isConfirm(true).build();
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
        TimeSlot timeSlot1 = new TimeSlot(1L);
        TimeSlot timeSlot2 = new TimeSlot(1L);

        // when
        System.out.println("=====Logic Start=====");

        boolean equals = timeSlot1.equals(timeSlot2);

        System.out.println("=====Logic End=====");
        // then
        assertThat(equals).isTrue();

    }
}