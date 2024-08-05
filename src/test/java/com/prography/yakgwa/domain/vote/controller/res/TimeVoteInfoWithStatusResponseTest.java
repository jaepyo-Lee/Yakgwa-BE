package com.prography.yakgwa.domain.vote.controller.res;

import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class TimeVoteInfoWithStatusResponseTest {
    @Autowired
    DummyCreater dummyCreater;
    @Autowired
    RepositoryDeleter deleter;

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void BEFORE_CONFIRM일때OF() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        LocalDateTime now = LocalDateTime.now();
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, now, false);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, now.plusHours(1L), false);

        // when
        System.out.println("=====Logic Start=====");

        TimeVoteInfoWithStatusResponse response = TimeVoteInfoWithStatusResponse.of(VoteStatus.BEFORE_CONFIRM, List.of(saveTimeSlot1, saveTimeSlot2), saveMeet);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(response.getMeetStatus()).isEqualTo(VoteStatus.BEFORE_CONFIRM),
                () -> assertThat(response.getTimeInfos().size()).isEqualTo(2),
                () -> assertThat(response.getVoteDate()).isNull());
    }

    @Test
    void CONFIRM일때OF() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        LocalDateTime now = LocalDateTime.now();
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, now, false);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, now.plusHours(1L), false);

        // when
        System.out.println("=====Logic Start=====");

        TimeVoteInfoWithStatusResponse response = TimeVoteInfoWithStatusResponse.of(VoteStatus.CONFIRM, List.of(saveTimeSlot1, saveTimeSlot2), saveMeet);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(response.getMeetStatus()).isEqualTo(VoteStatus.CONFIRM),
                () -> assertThat(response.getTimeInfos().size()).isEqualTo(2),
                () -> assertThat(response.getVoteDate()).isNull());
    }

    @Test
    void BEFORE_VOTE일때OF() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        LocalDateTime now = LocalDateTime.now();
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, now, false);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, now.plusHours(1L), false);

        // when
        System.out.println("=====Logic Start=====");

        TimeVoteInfoWithStatusResponse response = TimeVoteInfoWithStatusResponse.of(VoteStatus.BEFORE_VOTE, List.of(saveTimeSlot1, saveTimeSlot2), saveMeet);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(response.getMeetStatus()).isEqualTo(VoteStatus.BEFORE_VOTE),
                () -> assertThat(response.getTimeInfos().size()).isEqualTo(2),
                () -> assertThat(response.getVoteDate().getStartVoteDate()).isEqualTo(saveMeet.getPeriod().getStartDate()),
                () -> assertThat(response.getVoteDate().getEndVoteDate()).isEqualTo(saveMeet.getPeriod().getEndDate()));
    }

    @Test
    void VOTE일때of() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        LocalDateTime now = LocalDateTime.now();
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, now, false);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, now.plusHours(1L), false);

        // when
        System.out.println("=====Logic Start=====");

        TimeVoteInfoWithStatusResponse response = TimeVoteInfoWithStatusResponse.of(VoteStatus.VOTE, List.of(saveTimeSlot1, saveTimeSlot2), saveMeet);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(response.getMeetStatus()).isEqualTo(VoteStatus.VOTE),
                () -> assertThat(response.getTimeInfos().size()).isEqualTo(2),
                () -> assertThat(response.getVoteDate().getStartVoteDate()).isEqualTo(saveMeet.getPeriod().getStartDate()),
                () -> assertThat(response.getVoteDate().getEndVoteDate()).isEqualTo(saveMeet.getPeriod().getEndDate()));
    }
}