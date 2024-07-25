package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.common.DummyCreater;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.impl.TimeSlotReader;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.req.PlaceInfosByMeetStatus;
import com.prography.yakgwa.domain.vote.service.req.TimeInfosByMeetStatus;
import com.prography.yakgwa.global.format.exception.vote.AlreadyTimeConfirmVoteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.prography.yakgwa.domain.meet.entity.MeetStatus.BEFORE_VOTE;
import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class VoteServiceTest {
    @Autowired
    DummyCreater dummyCreater;

    @Autowired
    private MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    private VoteService voteService;
    @Autowired
    private TimeSlotReader timeSlotReader;

    @Test
    void 약과원_모임확정되지않았을때_투표안했을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = dummyCreater.createAndSavePlace(1L);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(place, saveMeet, false);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_VOTE),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces()).isEmpty());

    }

    @Test
    void 약과원_모임확정되지않았을때_투표했을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = dummyCreater.createAndSavePlace(1L);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(place, saveMeet, false);
        dummyCreater.createAndSavePlaceVote(saveUser, savePlaceSlot);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.VOTE),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(1));

    }

    @Test
    void 약과장_모임장소확정안되었을때_투표시간지났을때_최다득표가있을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 0);
        User saveUser1 = dummyCreater.createAndSaveUser(1L);
        User saveUser2 = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant1 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser1, MeetRole.LEADER);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);

        Place place1 = dummyCreater.createAndSavePlace(1L);
        Place place2 = dummyCreater.createAndSavePlace(2L);
        Place place3 = dummyCreater.createAndSavePlace(3L);
        PlaceSlot savePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(place1, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(place2, saveMeet, false);
        PlaceSlot savePlaceSlot3 = dummyCreater.createAndSavePlaceSlot(place3, saveMeet, false);

        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot1);
        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot2);
        dummyCreater.createAndSavePlaceVote(saveUser1, savePlaceSlot3);

        dummyCreater.createAndSavePlaceVote(saveUser2, savePlaceSlot1);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser1.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.CONFIRM),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(1));

    }

    @Test
    void 약과장_모임장소확정안되었을때_투표시간지났을때_동표일때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 0);
        User saveUser1 = dummyCreater.createAndSaveUser(1L);
        User saveUser2 = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant1 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser1, MeetRole.LEADER);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);

        Place place1 = dummyCreater.createAndSavePlace(1L);
        Place place2 = dummyCreater.createAndSavePlace(2L);
        Place place3 = dummyCreater.createAndSavePlace(3L);
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

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser1.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_CONFIRM),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(3));

    }

    @Test
    void 약과원_모임장소확정되었을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = dummyCreater.createAndSavePlace(1L);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(place, saveMeet, true);
        dummyCreater.createAndSavePlaceSlot(place, saveMeet, false);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.CONFIRM),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(1));

    }

    //=============모임시간=============

    @Test
    void 약과원_투표안했는데모임시간확정되었을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusDays(1L);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, time, true);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, time2, false);

        // when
        System.out.println("=====Logic Start=====");

        TimeInfosByMeetStatus timeInfoWithMeetStatus = voteService.findTimeInfoWithMeetStatus(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(timeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.CONFIRM),
                () -> assertThat(timeInfoWithMeetStatus.getTimeSlots().size()).isEqualTo(1));
    }

    @Test
    void 약과원_투표했는데모임시간확정되었을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusDays(1L);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, time, true);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, time2, false);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot, saveUser);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot2, saveUser);

        // when
        System.out.println("=====Logic Start=====");

        TimeInfosByMeetStatus timeInfoWithMeetStatus = voteService.findTimeInfoWithMeetStatus(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(timeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.CONFIRM),
                () -> assertThat(timeInfoWithMeetStatus.getTimeSlots().size()).isEqualTo(1));
    }

    @Test
    void 약과원_모임시간이지나지않았을때_확정안되었을때_투표안했을떄_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusDays(1L);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, time, false);

        // when
        System.out.println("=====Logic Start=====");

        TimeInfosByMeetStatus timeInfoWithMeetStatus = voteService.findTimeInfoWithMeetStatus(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(timeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_VOTE),
                () -> assertThat(timeInfoWithMeetStatus.getTimeSlots().size()).isEqualTo(0));
    }

    @Test
    void 약과원_모임시간이지나지않았을때_확정안되었을때_투표했을떄_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusDays(1L);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, time, false);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, time.plusDays(1L), false);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot, saveUser);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot2, saveUser);

        // when
        System.out.println("=====Logic Start=====");

        TimeInfosByMeetStatus timeInfoWithMeetStatus = voteService.findTimeInfoWithMeetStatus(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");

        // then
        assertAll(() -> assertThat(timeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.VOTE),
                () -> assertThat(timeInfoWithMeetStatus.getTimeSlots().size()).isEqualTo(2));
    }


    @Test
    void 시간투표_확정하기() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDay = now.plusDays(1L);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, now, false);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, nextDay, false);
        // when
        System.out.println("=====Logic Start=====");

        voteService.confirmTime(saveUser.getId(), saveMeet.getId(), saveTimeSlot.getId());

        System.out.println("=====Logic End=====");
        // then
        TimeSlot timeSlot = timeSlotReader.read(saveTimeSlot.getId());
        assertThat(timeSlot.getConfirm()).isEqualTo(Boolean.TRUE);

    }

    @Test
    void 이미확정되었는데_시간투표할때_예외() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1L, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1L);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDay = now.plusDays(1L);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, now, false);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet, nextDay, false);
        // when
        System.out.println("=====Logic Start=====");

        voteService.confirmTime(saveUser.getId(), saveMeet.getId(), saveTimeSlot.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThrows(AlreadyTimeConfirmVoteException.class, () -> voteService.confirmTime(saveUser.getId(), saveMeet.getId(), saveTimeSlot1.getId()));
    }

}