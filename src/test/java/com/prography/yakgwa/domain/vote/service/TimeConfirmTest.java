package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TimeConfirmTest extends IntegrationTestSupport {
    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 가장득표를많이한시간이있는경우() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);
        User saveUser3 = dummyCreater.createAndSaveUser(3);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser3, MeetRole.PARTICIPANT);
        LocalDateTime now1 = LocalDateTime.now();
        LocalDateTime now2 = LocalDateTime.now().plusDays(1);
        LocalDateTime now3 = LocalDateTime.now().plusDays(2);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, now1, false);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, now2, false);
        TimeSlot saveTimeSlot3 = dummyCreater.createAndSaveTimeSlot(saveMeet, now3, false);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser2);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser3);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot2, saveUser);
        // when
        System.out.println("=====Logic Start=====");

        boolean confirmMaxOf = timeConfirm.confirmMaxOf(saveMeet);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(confirmMaxOf).isTrue(),
                () -> assertThat(saveTimeSlot1.getConfirm()).isTrue(),
                () -> assertThat(saveTimeSlot2.getConfirm()).isFalse(),
                () -> assertThat(saveTimeSlot3.getConfirm()).isFalse());
    }

    @Test
    void 동표인경우() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);
        User saveUser3 = dummyCreater.createAndSaveUser(3);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser3, MeetRole.PARTICIPANT);
        LocalDateTime now1 = LocalDateTime.now();
        LocalDateTime now2 = LocalDateTime.now().plusDays(1);
        LocalDateTime now3 = LocalDateTime.now().plusDays(2);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, now1, false);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, now2, false);
        TimeSlot saveTimeSlot3 = dummyCreater.createAndSaveTimeSlot(saveMeet, now3, false);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot2, saveUser2);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot3, saveUser3);

        // when
        System.out.println("=====Logic Start=====");

        boolean confirmMaxOf = timeConfirm.confirmMaxOf(saveMeet);
        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(confirmMaxOf).isFalse(),
                ()-> assertThat(saveTimeSlot1.getConfirm()).isFalse(),
                ()-> assertThat(saveTimeSlot2.getConfirm()).isFalse(),
                ()-> assertThat(saveTimeSlot3.getConfirm()).isFalse());
    }
}