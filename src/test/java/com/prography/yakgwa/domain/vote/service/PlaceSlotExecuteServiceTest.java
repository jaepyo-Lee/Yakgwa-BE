package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmException;
import com.prography.yakgwa.global.format.exception.vote.NotValidVotePlaceException;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class PlaceSlotExecuteServiceTest {
    @Autowired
    DummyCreater dummyCreater;

    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;

    @Autowired
    @Qualifier("placeVoteExecuteService")
    VoteExecuter<PlaceVote, Set<Long>> executer;

    @Autowired
    RepositoryDeleter deleter;

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    /*================votePlace===============*/
    @Test
    void 이미장소가확정되어있는경우장소투표_예외() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(AlreadyPlaceConfirmException.class, () -> executer.vote(saveUser.getId(), saveMeet.getId(), Set.of(1L)));
        System.out.println("=====Logic End=====");
    }

    @Test
    void 추가가안되어있는장소후보의장소투표_예외() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(NotValidVotePlaceException.class, () -> executer.vote(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot.getId() + 1L)));
        System.out.println("=====Logic End=====");
    }

    @Test
    void 장소투표() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        Place savePlace1 = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        PlaceSlot savePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(savePlace1, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);
        // when
        System.out.println("=====Logic Start=====");

        List<PlaceVote> placeVotes = executer.vote(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot1.getId(), savePlaceSlot2.getId()));

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(placeVotes.size()).isEqualTo(2));

    }

    @Test
    void 모임에참여중인아닌데장소투표_예외() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);

        Place savePlace1 = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        PlaceSlot savePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(savePlace1, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);
        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(NotFoundParticipantException.class, () -> executer.vote(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot1.getId(), savePlaceSlot2.getId())));
        System.out.println("=====Logic End=====");
    }
}