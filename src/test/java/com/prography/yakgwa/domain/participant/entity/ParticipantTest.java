package com.prography.yakgwa.domain.participant.entity;

import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {
    @Test
    void 약과장일떄() {
        // given
        Participant participant = Participant.builder().meetRole(MeetRole.LEADER).build();

        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThat(participant.isLeader()).isTrue();

        System.out.println("=====Logic End=====");
    }

    @Test
    void 약과원일떄() {
        // given
        Participant participant = Participant.builder().meetRole(MeetRole.PARTICIPANT).build();


        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThat(participant.isLeader()).isFalse();


        System.out.println("=====Logic End=====");
    }
}