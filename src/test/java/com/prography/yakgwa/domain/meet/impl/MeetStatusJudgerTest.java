package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.prography.yakgwa.domain.meet.entity.MeetStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class MeetStatusJudgerTest {
    @Autowired
    private MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    private MeetStatusJudger meetStatusJudger;

    @Autowired
    RepositoryDeleter deleter;
    @Autowired
    DummyCreater dummyCreater;

    @AfterEach
    void initial(){
        deleter.deleteAll();
    }

    @Test
    void 모임이확정상태일때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        Place place = dummyCreater.createAndSavePlace(1);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), Boolean.TRUE);
        PlaceSlot andSavePlaceSlot = dummyCreater.createAndSavePlaceSlot(place, saveMeet, Boolean.TRUE);
        User saveUser =dummyCreater. createAndSaveUser(1);

        // when
        System.out.println("=====Logic Start=====");

        MeetStatus meetStatus = meetStatusJudger.judgeStatusOf(saveMeet, saveUser);

        System.out.println("=====Logic End=====");
        // then
        assertThat(meetStatus).isEqualTo(CONFIRM);
    }

    @Transactional
    @Test
    void 모임의시간이지나서_장소와시간투표두개모두에최다득표가없어서_BEFORE_CONFIRM_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, -1);
        Place place1 = dummyCreater.createAndSavePlace(1);
        Place place2 = dummyCreater.createAndSavePlace(2);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), Boolean.FALSE);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now().plusDays(1L), Boolean.FALSE);
        PlaceSlot andSavePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        PlaceSlot andSavePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        User saveUser1 = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);

        TimeVote andSaveTimeVote1 = dummyCreater.createAndSaveTimeVote(saveTimeSlot1, saveUser1);
        TimeVote andSaveTimeVote2 =dummyCreater. createAndSaveTimeVote(saveTimeSlot2, saveUser2);

        PlaceVote andSavePlaceVote1 = dummyCreater.createAndSavePlaceVote(saveUser1, andSavePlaceSlot1);
        PlaceVote andSavePlaceVote2 = dummyCreater.createAndSavePlaceVote(saveUser2, andSavePlaceSlot2);

        // when
        System.out.println("=====Logic Start=====");

        MeetStatus meetStatus = meetStatusJudger.judgeStatusOf(saveMeet, saveUser1);

        System.out.println("=====Logic End=====");
        // then
        assertThat(meetStatus).isEqualTo(BEFORE_CONFIRM);
    }

    @Transactional
    @Test
    void 모임의시간이지나기전_확정되지않은상태_하나라도투표를해서_VOTE_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet =dummyCreater. createAndSaveMeet(1, theme, 24);
        Place place1 =dummyCreater. createAndSavePlace(1);
        Place place2 = dummyCreater.createAndSavePlace(2);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), Boolean.FALSE);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now().plusDays(1L), Boolean.FALSE);
        PlaceSlot andSavePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        PlaceSlot andSavePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        User saveUser1 = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);

        PlaceVote andSavePlaceVote1 =dummyCreater. createAndSavePlaceVote(saveUser1, andSavePlaceSlot1);

        // when
        System.out.println("=====Logic Start=====");

        MeetStatus meetStatus = meetStatusJudger.judgeStatusOf(saveMeet, saveUser1);

        System.out.println("=====Logic End=====");
        // then
        assertThat(meetStatus).isEqualTo(VOTE);
    }

    @Transactional
    @Test
    void 모임의시간이지나기전_확정되지않은상태_투표하지않아서_BEFORE_VOTE_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        Place place1 = dummyCreater.createAndSavePlace(1);
        Place place2 = dummyCreater.createAndSavePlace(2);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), Boolean.FALSE);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now().plusDays(1L), Boolean.FALSE);
        PlaceSlot andSavePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        PlaceSlot andSavePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        User saveUser1 =dummyCreater. createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);

        // when
        System.out.println("=====Logic Start=====");

        MeetStatus meetStatus = meetStatusJudger.judgeStatusOf(saveMeet, saveUser1);

        System.out.println("=====Logic End=====");
        // then
        assertThat(meetStatus).isEqualTo(BEFORE_VOTE);
    }

    /*===================verifyConfirmAndConfirmPlacePossible==================*/
    @Test
    void 최다득표구하는() {
        // given


        // when
        System.out.println("=====Logic Start=====");



        System.out.println("=====Logic End=====");
        // then
    }
}