package com.prography.yakgwa.domain.meet.controller.res;

import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class PostConfirmMeetInfoResponseTest {
    @Autowired
    DummyCreater dummyCreater;
    @Autowired
    RepositoryDeleter deleter;
    @AfterEach
    void init(){
        deleter.deleteAll();
    }
    @Test
    void of테스트() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), true);
        MeetWithVoteAndStatus build1 = MeetWithVoteAndStatus.builder().meetStatus(MeetStatus.CONFIRM).meet(saveMeet).placeSlot(savePlaceSlot).timeSlot(saveTimeSlot).build();
        MeetWithVoteAndStatus build2 = MeetWithVoteAndStatus.builder().meetStatus(MeetStatus.CONFIRM).meet(saveMeet).placeSlot(null).timeSlot(saveTimeSlot).build();
        MeetWithVoteAndStatus build3 = MeetWithVoteAndStatus.builder().meetStatus(MeetStatus.CONFIRM).meet(saveMeet).placeSlot(null).timeSlot(saveTimeSlot).build();
        MeetWithVoteAndStatus build4 = MeetWithVoteAndStatus.builder().meetStatus(MeetStatus.CONFIRM).meet(saveMeet).placeSlot(savePlaceSlot).timeSlot(saveTimeSlot).build();
        List<MeetWithVoteAndStatus> build = List.of(build1, build2, build3, build4);
        // when
        System.out.println("=====Logic Start=====");

        PostConfirmMeetInfoResponse postConfirmMeetInfoResponse = PostConfirmMeetInfoResponse.of(build);

        System.out.println("=====Logic End=====");
        // then
        assertThat(postConfirmMeetInfoResponse.getMeetInfosWithStatus().size()).isEqualTo(4);

    }
}