package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.common.DummyCreater;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.req.PlaceInfosByMeetStatus;
import com.prography.yakgwa.domain.vote.service.req.TimeInfosByMeetStatus;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import com.prography.yakgwa.global.format.exception.slot.NotMatchSlotInMeetException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmVoteException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyTimeConfirmVoteException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    private UserJpaRepository userJpaRepository;
    @Autowired
    private MeetJpaRepository meetJpaRepository;
    @Autowired
    private PlaceJpaRepository placeJpaRepository;
    @Autowired
    private PlaceSlotJpaRepository placeSlotJpaRepository;
    @Autowired
    private TimeVoteJpaRepository timeVoteJpaRepository;
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    private PlaceVoteJpaRepository placeVoteJpaRepository;
    @Autowired
    private ParticipantJpaRepository participantJpaRepository;

    @AfterEach
    void init() {
        participantJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        placeVoteJpaRepository.deleteAll();
        placeSlotJpaRepository.deleteAll();
        placeJpaRepository.deleteAll();
        timeVoteJpaRepository.deleteAll();
        timeSlotJpaRepository.deleteAll();
    }

    @Test
    void 약과원_모임확정되지않았을때_투표안했을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = dummyCreater.createAndSavePlace(1);
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
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = dummyCreater.createAndSavePlace(1);
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
    void 약과원_모임장소확정안되었을때_투표시간지났을때_최다득표가없을때_조회() {
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

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser2.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(() -> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_CONFIRM),
                () -> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(3));

    }

    @Test
    void 약과장_모임장소확정안되었을때_투표시간지났을때_최다득표가있을때_조회() {
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
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = dummyCreater.createAndSavePlace(1);
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
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
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
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
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
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
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
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
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
    void 이미확정되었는데_시간투표할때_예외() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
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

    /*==================confirmPlace====================*/
    @Test
    void 장소확정() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);

        // when
        System.out.println("=====Logic Start=====");

        voteService.confirmPlace(saveUser.getId(), saveMeet.getId(), savePlaceSlot.getId());

        System.out.println("=====Logic End=====");
        // then
        PlaceSlot comparisonPlaceSlot = PlaceSlot.builder()
                .id(savePlaceSlot.getId())
                .confirm(true)
                .meet(savePlaceSlot.getMeet())
                .place(savePlaceSlot.getPlace())
                .build();
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(saveMeet.getId());
        assertAll(() -> assertThat(placeSlots.size()).isEqualTo(1),
                () -> assertThat(placeSlots).allMatch(PlaceSlot::getConfirm));
    }

    @Test
    void 이미확정되어있을떄_장소확정_예외() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);

        // when
        //then
        System.out.println("=====Logic Start=====");

        assertThrows(AlreadyPlaceConfirmVoteException.class, () -> voteService.confirmPlace(saveUser.getId(), saveMeet.getId(), savePlaceSlot.getId()));

        System.out.println("=====Logic End=====");
    }

    @Test
    void 해당모임의장소후보가아닌후보_장소확정_예외() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Meet saveMeet2 = dummyCreater.createAndSaveMeet(2, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet2, false);

        // when
        //then
        System.out.println("=====Logic Start=====");

        assertThrows(NotMatchSlotInMeetException.class, () -> voteService.confirmPlace(saveUser.getId(), saveMeet.getId(), savePlaceSlot2.getId()));

        System.out.println("=====Logic End=====");
    }


    @Test
    void 참여중이아닐때_장소확정_예외() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);


        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(NotFoundParticipantException.class, () -> voteService.confirmPlace(saveUser.getId(), saveMeet.getId(), savePlaceSlot.getId()));

        System.out.println("=====Logic End=====");
    }

    /*=========confirmTime==========*/

    @Test
    void 시간확정() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);

        // when
        System.out.println("=====Logic Start=====");

        voteService.confirmTime(saveUser.getId(), saveMeet.getId(), saveTimeSlot.getId());

        System.out.println("=====Logic End=====");
        // then
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllByMeetId(saveMeet.getId());

        assertAll(() -> assertThat(timeSlots.size()).isEqualTo(1),
                () -> assertThat(timeSlots).allMatch(TimeSlot::getConfirm));
    }

    @Test
    void 이미확정이되었을때_시간확정_예외() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), true);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(AlreadyTimeConfirmVoteException.class, () -> voteService.confirmTime(saveUser.getId(), saveMeet.getId(), saveTimeSlot.getId()));
        System.out.println("=====Logic End=====");
    }

    @Test
    void 참여중이아닐때_시간확정_예외() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);
        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(NotFoundParticipantException.class, () -> voteService.confirmTime(saveUser.getId(), saveMeet.getId(), saveTimeSlot.getId()));
        System.out.println("=====Logic End=====");
    }
}