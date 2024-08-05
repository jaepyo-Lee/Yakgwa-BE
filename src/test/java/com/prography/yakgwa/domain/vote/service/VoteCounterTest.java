package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class VoteCounterTest {
    @Autowired
    VoteCounter voteCounter;
    @Autowired
    DummyCreater dummyCreater;

    @Autowired
    RepositoryDeleter deleter;

    @AfterEach
    void init(){
        deleter.deleteAll();
    }

    @Test
    void 최대투표장소발견() {
        // given
        Place savePlace = dummyCreater.createAndSavePlace(1);
        Place savePlace1 = dummyCreater.createAndSavePlace(2);
        Place savePlace2 = dummyCreater.createAndSavePlace(3);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser1 = dummyCreater.createAndSaveUser(2);
        User saveUser2 = dummyCreater.createAndSaveUser(3);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceSlot savePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(savePlace1, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);

        dummyCreater.createAndSavePlaceVote(saveUser, savePlaceSlot);
        dummyCreater.createAndSavePlaceVote(saveUser, savePlaceSlot1);
        dummyCreater.createAndSavePlaceVote(saveUser, savePlaceSlot2);

        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot);
        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot1);

        dummyCreater.createAndSavePlaceVote(saveUser2, savePlaceSlot);

        // when
        System.out.println("=====Logic Start=====");

        List<PlaceSlot> maxVotePlaceSlots = voteCounter.findMaxVotePlaceSlotFrom(saveMeet);

        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(maxVotePlaceSlots.size()).isEqualTo(1));
    }

    @Test
    void 최대투표장소가여러개() {
        // given
        Place savePlace = dummyCreater.createAndSavePlace(1);
        Place savePlace1 = dummyCreater.createAndSavePlace(2);
        Place savePlace2 = dummyCreater.createAndSavePlace(3);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser1 = dummyCreater.createAndSaveUser(2);
        User saveUser2 = dummyCreater.createAndSaveUser(3);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceSlot savePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(savePlace1, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);

        dummyCreater.createAndSavePlaceVote(saveUser, savePlaceSlot);
        dummyCreater.createAndSavePlaceVote(saveUser, savePlaceSlot2);

        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot);
        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot1);

        dummyCreater.createAndSavePlaceVote(saveUser2, savePlaceSlot1);

        // when
        System.out.println("=====Logic Start=====");

        List<PlaceSlot> maxVotePlaceSlots = voteCounter.findMaxVotePlaceSlotFrom(saveMeet);

        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(maxVotePlaceSlots.size()).isEqualTo(2));
    }

    @Test
    void 최대투표시간발견() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowParse = LocalDateTime.parse(now.format(formatter), formatter);
        LocalDateTime after1hParse = LocalDateTime.parse(now.plusHours(1L).format(formatter), formatter);

        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, nowParse, false);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, after1hParse, false);

        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser1 = dummyCreater.createAndSaveUser(2);
        User saveUser2 = dummyCreater.createAndSaveUser(3);

        dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser1);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser2);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot, saveUser);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot, saveUser1);

        // when
        System.out.println("=====Logic Start=====");

        List<TimeSlot> maxVoteTimeSlotFrom = voteCounter.findMaxVoteTimeSlotFrom(saveMeet);

        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(maxVoteTimeSlotFrom.size()).isEqualTo(1));
    }
    @Test
    void 최대투표시간여러개() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowParse = LocalDateTime.parse(now.format(formatter), formatter);
        LocalDateTime after1hParse = LocalDateTime.parse(now.plusHours(1L).format(formatter), formatter);

        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, nowParse, false);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, after1hParse, false);

        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser1 = dummyCreater.createAndSaveUser(2);
        User saveUser2 = dummyCreater.createAndSaveUser(3);

        dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser1);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser2);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot, saveUser);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot, saveUser1);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot, saveUser2);

        // when
        System.out.println("=====Logic Start=====");

        List<TimeSlot> maxVoteTimeSlotFrom = voteCounter.findMaxVoteTimeSlotFrom(saveMeet);

        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(maxVoteTimeSlotFrom.size()).isEqualTo(2));
    }
}