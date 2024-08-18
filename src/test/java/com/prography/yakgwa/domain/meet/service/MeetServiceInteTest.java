package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.meet.service.res.MeetInfoWithParticipant;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MeetServiceInteTest extends IntegrationTestSupport {

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    /*====================findWithParticipant====================*/

    @Test
    void 모임의참여원조회() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser1 = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);
        Participant saveParticipant1 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser1, MeetRole.LEADER);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);

        // when
        System.out.println("=====Logic Start=====");

        MeetInfoWithParticipant meetInfoWithParticipant = meetService.findWithParticipant(saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(meetInfoWithParticipant.getMeet().getId()).isEqualTo(saveMeet.getId()),
                () -> assertThat(meetInfoWithParticipant.getParticipants().size()).isEqualTo(2));
    }

    @Transactional
    @Test
    void 탐퇴한회원이있는모임의참여원조회() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser1 = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);
        Participant saveParticipant1 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser1, MeetRole.LEADER);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);

        String baseImage = "baseImage";
        saveUser2.signout(baseImage);



        // when
        System.out.println("=====Logic Start=====");

        MeetInfoWithParticipant meetInfoWithParticipant = meetService.findWithParticipant(saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(meetInfoWithParticipant.getMeet().getId()).isEqualTo(saveMeet.getId()),
                () -> assertThat(meetInfoWithParticipant.getParticipants()).hasSize(2).extracting(participant -> participant.getUser().getAuthId())
                        .containsNull());
    }

    /*====================findWithStatus====================*/


    @Test
    void 확정되고투표중인된모임들의상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);

        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), true);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        Meet saveMeet2 = dummyCreater.createAndSaveMeet(2, saveMeetTheme, 24);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet2, false);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet2, LocalDateTime.now(), true);
        dummyCreater.createAndSaveParticipant(saveMeet2, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(withStatus.size()).isEqualTo(2);
    }

    @Test
    void 모임시간이3시간이상지났을때모임상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, -1);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now().minusHours(4L), true);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);


        List<MeetWithVoteAndStatus> meetWithVoteAndStatuses = List.of(MeetWithVoteAndStatus.of(saveMeet, saveTimeSlot, savePlaceSlot, MeetStatus.CONFIRM));
        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(withStatus.size()).isEqualTo(0);
    }

    @Test
    void 모임시간이3시간이상지나지않았을때모임상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, -1);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), true);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);


        List<MeetWithVoteAndStatus> meetWithVoteAndStatuses = List.of(MeetWithVoteAndStatus.of(saveMeet, saveTimeSlot, savePlaceSlot, MeetStatus.CONFIRM));
        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(withStatus.size()).isEqualTo(1);
    }

    @Test
    void 모임시간확정에실패하고확정가능시간을지났을때모임상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, -30);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now().plusHours(5L), false);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(withStatus.size()).isEqualTo(0);
    }

    @Test
    void 장소가확정안된투표시간이지나지않은모임의상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), true);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(withStatus.size()).isEqualTo(1),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeetStatus).isEqualTo(List.of(MeetStatus.BEFORE_VOTE)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeet).isEqualTo(List.of(saveMeet)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getTimeSlot).containsExactly(saveTimeSlot),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getPlaceSlot).containsExactly((PlaceSlot) null));
    }

    @Test
    void 시간이확정안된투표시간이지나지않은모임의상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(withStatus.size()).isEqualTo(1),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeetStatus).isEqualTo(List.of(MeetStatus.BEFORE_VOTE)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeet).isEqualTo(List.of(saveMeet)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getTimeSlot).containsExactly((TimeSlot) null),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getPlaceSlot).containsExactly(savePlaceSlot));
    }

    @Test
    void 모두확정안된투표시간이지나지않은모임의상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(withStatus.size()).isEqualTo(1),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeetStatus).isEqualTo(List.of(MeetStatus.BEFORE_VOTE)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeet).isEqualTo(List.of(saveMeet)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getTimeSlot).containsExactly((TimeSlot) null),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getPlaceSlot).containsExactly((PlaceSlot) null));
    }

    /*=================findPostConfirm=================*/

    /**
     * Todo
     * Work) 변경된 로직에 따라 테코 수정하기
     * Write-Date)
     * Finish-Date)
     */

    @Test
    void 나의_확정된_모임조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);

        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now().minusHours(2L), true);

        Meet saveMeet2 = dummyCreater.createAndSaveMeet(2, saveMeetTheme, 24);
        dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet2, true);
        dummyCreater.createAndSaveTimeSlot(saveMeet2, LocalDateTime.now(), true);
        dummyCreater.createAndSaveParticipant(saveMeet2, saveUser, MeetRole.LEADER);

        Meet saveMeet3 = dummyCreater.createAndSaveMeet(3, saveMeetTheme, -24);
        dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet3, true);
        dummyCreater.createAndSaveTimeSlot(saveMeet3, LocalDateTime.now().minusHours(2L), false);
        dummyCreater.createAndSaveParticipant(saveMeet3, saveUser, MeetRole.LEADER);

        Meet saveMeet4 = dummyCreater.createAndSaveMeet(4, saveMeetTheme, 24);
        dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet4, false);
        dummyCreater.createAndSaveTimeSlot(saveMeet4, LocalDateTime.now().minusHours(2L), false);
        dummyCreater.createAndSaveParticipant(saveMeet4, saveUser, MeetRole.LEADER);
        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> postConfirm = meetService.findPostConfirm(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(postConfirm.size()).isEqualTo(3);
    }
}