package com.prography.yakgwa.domain.vote.service.place;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import com.prography.yakgwa.global.format.exception.slot.NotFoundPlaceSlotException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmException;
import com.prography.yakgwa.global.format.exception.vote.NotValidConfirmTimeException;
import com.prography.yakgwa.global.format.exception.vote.NotValidVotePlaceException;
import com.prography.yakgwa.global.format.exception.vote.NotValidVoteTimeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlaceVoteExecuteServiceTest extends IntegrationTestSupport {

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    /**
     * 투표할때 테스트코드
     */
    @Test
    void 투표하는장소가모임장소후보에없는경우() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);
        User saveUser = dummyCreater.createAndSaveUser(1);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        // when
        System.out.println("=====Logic Start=====");
        System.out.println("=====Logic End=====");
        // then
        assertThrows(NotValidVotePlaceException.class, () -> placeVoteExecuteService.vote(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot.getId() + savePlaceSlot2.getId())));
    }

    @Test
    void 투표시간이이미지나투표가불가한경우() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, -25);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);
        User saveUser = dummyCreater.createAndSaveUser(1);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");


        System.out.println("=====Logic End=====");
        // then
        assertThrows(NotValidVoteTimeException.class, () -> placeVoteExecuteService.vote(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot.getId() + savePlaceSlot2.getId())));
    }

    @Test
    void 정상투표() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);
        User saveUser = dummyCreater.createAndSaveUser(1);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");

        List<PlaceVote> vote = placeVoteExecuteService.vote(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot.getId(), savePlaceSlot2.getId()));

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(vote.size()).isEqualTo(2));
    }

    @Test
    void 이미장소가확정된경우() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, true);
        User saveUser = dummyCreater.createAndSaveUser(1);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");

        assertThrows(AlreadyPlaceConfirmException.class, () -> placeVoteExecuteService.vote(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot.getId() + savePlaceSlot2.getId())));


        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 이미투표한경우기존투표를삭제하고재투표하는지확인() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);
        User saveUser = dummyCreater.createAndSaveUser(1);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        List<PlaceVote> vote = placeVoteExecuteService.vote(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot.getId(), savePlaceSlot2.getId()));

        // when
        System.out.println("=====Logic Start=====");

        List<PlaceVote> vote1 = placeVoteExecuteService.vote(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot.getId()));

        System.out.println("=====Logic End=====");
        // then
        List<PlaceVote> allByUserIdAndMeetId = placeVoteJpaRepository.findAllByUserIdAndMeetId(saveUser.getId(), saveMeet.getId());
        assertThat(allByUserIdAndMeetId.size()).isEqualTo(1);
    }

    @Test
    void 확정하기() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);

        // when
        System.out.println("=====Logic Start=====");

        placeVoteExecuteService.confirm(saveMeet.getId(), saveUser.getId(), savePlaceSlot.getId());
        System.out.println("=====Logic End=====");
        // then
        assertThat(savePlaceSlot.getConfirm()).isTrue();

    }

    @Test
    void 존재하는장소후보가아닌경우_예외() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);

        // when
        System.out.println("=====Logic Start=====");

        assertThrows(NotFoundPlaceSlotException.class, () -> placeVoteExecuteService.confirm(saveMeet.getId(), saveUser.getId(), savePlaceSlot.getId() + 1));

        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 모임참여자가아닌경우_예외() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);

        // when
        System.out.println("=====Logic Start=====");

        assertThrows(NotFoundParticipantException.class, () -> placeVoteExecuteService.confirm(saveMeet.getId(), saveUser.getId(), savePlaceSlot.getId()));


        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 확정가능시간을지났을때_예외() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        int passConfirmTimeValidHour = -50;
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, passConfirmTimeValidHour);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);

        // when
        System.out.println("=====Logic Start=====");
        assertThrows(NotValidConfirmTimeException.class, () -> placeVoteExecuteService.confirm(saveMeet.getId(), saveUser.getId(), savePlaceSlot.getId()));


        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 이미확정되어있을때예외() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        int passConfirmTimeValidHour = -50;
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, passConfirmTimeValidHour);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);

        // when
        System.out.println("=====Logic Start=====");

        assertThrows(AlreadyPlaceConfirmException.class, () -> placeVoteExecuteService.confirm(saveMeet.getId(), saveUser.getId(), savePlaceSlot.getId()));

        System.out.println("=====Logic End=====");
        // then
    }
}