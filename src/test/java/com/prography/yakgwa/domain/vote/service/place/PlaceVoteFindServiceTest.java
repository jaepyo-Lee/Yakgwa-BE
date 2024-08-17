package com.prography.yakgwa.domain.vote.service.place;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.service.place.res.PlaceInfosByMeetStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
class PlaceVoteFindServiceTest extends IntegrationTestSupport {

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    // 시간과 장소는 나뉘어져있기 때문에 나눠서 생각하면됌
    // 1. 확정되었을때 Confirm
    // 2. 투표는끝났지만 확정이 안되었을떄 Before_Confirm
    // 3. 마감전 투표를 아직 안했을때
    // 4. 마감전 투표를 했을때
    @Test
    void 약과원_모임장소확정되었을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(place, saveMeet, true);


        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = placeVoteFinder.findVoteInfoWithStatusOf(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.CONFIRM),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(1));

    }


    @Test
    void 약과원_모임장소확정안되었을때_투표시간지났을때_최다득표가없을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, -1);
        User saveUser1 = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant1 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser1, MeetRole.LEADER);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);

        Place place1 = dummyCreater.createAndSavePlace(1);
        Place place2 = dummyCreater.createAndSavePlace(2);
        Place place3 = dummyCreater.createAndSavePlace(3);
        PlaceSlot savePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(place1, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(place2, saveMeet, false);
        PlaceSlot savePlaceSlot3 = dummyCreater.createAndSavePlaceSlot(place3, saveMeet, false);

        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot1);
        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot2);
        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot3);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = placeVoteFinder.findVoteInfoWithStatusOf(saveUser2.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_CONFIRM),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(3));

    }



    @Test
    void 약과원_모임장소확정되지않았을때_투표안했을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(place, saveMeet, false);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = placeVoteFinder.findVoteInfoWithStatusOf(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_VOTE),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces()).isEmpty());

    }

    @Test
    void 약과원_모임장소확정되지않았을때_투표했을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(place, saveMeet, false);
        dummyCreater.createAndSavePlaceVote(saveUser, savePlaceSlot);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = placeVoteFinder.findVoteInfoWithStatusOf(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.VOTE),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(1));

    }


    @Test
    void 약과장_모임장소확정안되었을때_투표시간지났을때_최다득표가있을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, -1);
        User saveUser1 = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant1 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser1, MeetRole.LEADER);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);

        Place place1 = dummyCreater.createAndSavePlace(1);
        Place place2 = dummyCreater.createAndSavePlace(2);
        Place place3 = dummyCreater.createAndSavePlace(3);
        PlaceSlot savePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(place1, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(place2, saveMeet, false);
        PlaceSlot savePlaceSlot3 = dummyCreater.createAndSavePlaceSlot(place3, saveMeet, false);

        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot1);
        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot2);
        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot3);

        dummyCreater.createAndSavePlaceVote(saveUser2, savePlaceSlot1);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = placeVoteFinder.findVoteInfoWithStatusOf(saveUser1.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_CONFIRM));

    }

    @Test
    void 약과장_모임장소확정안되었을때_투표시간지났을때_동표일때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 0);
        User saveUser1 = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant1 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser1, MeetRole.LEADER);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);

        Place place1 = dummyCreater.createAndSavePlace(1);
        Place place2 = dummyCreater.createAndSavePlace(2);
        Place place3 = dummyCreater.createAndSavePlace(3);
        PlaceSlot savePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(place1, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(place2, saveMeet, false);
        PlaceSlot savePlaceSlot3 = dummyCreater.createAndSavePlaceSlot(place3, saveMeet, false);

        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot1);
        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot2);
        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot3);

        dummyCreater.createAndSavePlaceVote(saveUser2, savePlaceSlot1);
        dummyCreater.createAndSavePlaceVote(saveUser2, savePlaceSlot2);
        dummyCreater.createAndSavePlaceVote(saveUser2, savePlaceSlot3);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = placeVoteFinder.findVoteInfoWithStatusOf(saveUser1.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_CONFIRM),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(3));

    }


}