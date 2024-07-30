package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.common.DummyCreater;
import com.prography.yakgwa.domain.common.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.service.req.EnableTimeRequestDto;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmVoteException;
import com.prography.yakgwa.global.format.exception.vote.NotValidVotePlaceException;
import com.prography.yakgwa.global.format.exception.vote.NotValidVoteTimeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class VoteExecuteServiceTest {
    @Autowired
    DummyCreater dummyCreater;

    @Autowired
    VoteExecuteService voteExecuteService;
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    RepositoryDeleter deleter;

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 이미있던시간후보의시간투표() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        LocalDateTime after1h = LocalDateTime.parse(LocalDateTime.now().plusHours(1L).format(formatter), formatter);
        LocalDateTime after2h = LocalDateTime.parse(LocalDateTime.now().plusHours(2L).format(formatter), formatter);

        dummyCreater.createAndSaveTimeSlot(saveMeet, now, false);
        dummyCreater.createAndSaveTimeSlot(saveMeet, after1h, false);
        dummyCreater.createAndSaveTimeSlot(saveMeet, after2h, false);

        EnableTimeRequestDto request = EnableTimeRequestDto.builder()
                .enableTimes(Set.of(now, after1h, after2h))
                .build();
        // when
        System.out.println("=====Logic Start=====");

        List<TimeVote> timeVotes = voteExecuteService.voteTime(saveUser.getId(), saveMeet.getId(), request);

        System.out.println("=====Logic End=====");
        // then
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllByMeetId(saveMeet.getId());

        assertAll(() -> assertThat(timeSlots.size()).isEqualTo(3),
                () -> assertThat(timeVotes.size()).isEqualTo(3));
    }

    @Test
    void 이미있던시간후보가아닌시간의시간투표() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        LocalDateTime after1h = LocalDateTime.parse(LocalDateTime.now().plusHours(1L).format(formatter), formatter);
        LocalDateTime after2h = LocalDateTime.parse(LocalDateTime.now().plusHours(2L).format(formatter), formatter);
        LocalDateTime after3h = LocalDateTime.parse(LocalDateTime.now().plusHours(3L).format(formatter), formatter);

        dummyCreater.createAndSaveTimeSlot(saveMeet, now, false);
        dummyCreater.createAndSaveTimeSlot(saveMeet, after1h, false);
        dummyCreater.createAndSaveTimeSlot(saveMeet, after2h, false);

        EnableTimeRequestDto request = EnableTimeRequestDto.builder()
                .enableTimes(Set.of(after3h))
                .build();
        // when
        System.out.println("=====Logic Start=====");

        List<TimeVote> timeVotes = voteExecuteService.voteTime(saveUser.getId(), saveMeet.getId(), request);

        System.out.println("=====Logic End=====");
        // then
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllByMeetId(saveMeet.getId());

        assertAll(() -> assertThat(timeSlots.size()).isEqualTo(4),
                () -> assertThat(timeVotes.size()).isEqualTo(1));
    }

    @Test
    void 새로운시간후보의_시간투표() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        // 원하는 형식 지정 (주의: 공백도 형식에 포함되어야 합니다)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 현재 시간을 원하는 형식으로 포맷 후 다시 LocalDateTime으로 파싱
        LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        LocalDateTime after1h = LocalDateTime.parse(LocalDateTime.now().plusHours(1L).format(formatter), formatter);
        LocalDateTime after2h = LocalDateTime.parse(LocalDateTime.now().plusHours(2L).format(formatter), formatter);
        EnableTimeRequestDto request = EnableTimeRequestDto.builder()
                .enableTimes(Set.of(now, after1h, after2h))
                .build();
        // when
        System.out.println("=====Logic Start=====");

        List<TimeVote> timeVotes = voteExecuteService.voteTime(saveUser.getId(), saveMeet.getId(), request);

        System.out.println("=====Logic End=====");
        // then
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllByMeetId(saveMeet.getId());

        assertAll(() -> assertThat(timeSlots.size()).isEqualTo(3),
                () -> assertThat(timeVotes.size()).isEqualTo(3));
    }

    @Test
    void 해당모임에_찾여중이아닐때_시간투표_예외() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        // 원하는 형식 지정 (주의: 공백도 형식에 포함되어야 합니다)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 현재 시간을 원하는 형식으로 포맷 후 다시 LocalDateTime으로 파싱
        LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        LocalDateTime after1h = LocalDateTime.parse(LocalDateTime.now().plusHours(1L).format(formatter), formatter);
        LocalDateTime after2h = LocalDateTime.parse(LocalDateTime.now().plusHours(2L).format(formatter), formatter);
        EnableTimeRequestDto request = EnableTimeRequestDto.builder()
                .enableTimes(Set.of(now, after1h, after2h))
                .build();
        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(NotFoundParticipantException.class, () -> voteExecuteService.voteTime(saveUser.getId(), saveMeet.getId(), request));
    }

    @Test
    void 해당모임의시간이확정되었을때_시간투표_예외() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);
        // 원하는 형식 지정 (주의: 공백도 형식에 포함되어야 합니다)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 현재 시간을 원하는 형식으로 포맷 후 다시 LocalDateTime으로 파싱
        LocalDateTime overPeriod = LocalDateTime.parse(LocalDateTime.now().plusDays(2L).format(formatter), formatter);
        EnableTimeRequestDto request = EnableTimeRequestDto.builder()
                .enableTimes(Set.of(overPeriod))
                .build();
        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(NotValidVoteTimeException.class, () -> voteExecuteService.voteTime(saveUser.getId(), saveMeet.getId(), request));
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
        assertThrows(AlreadyPlaceConfirmVoteException.class, () -> voteExecuteService.votePlace(saveUser.getId(), saveMeet.getId(), Set.of(1L)));
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
        assertThrows(NotValidVotePlaceException.class, () -> voteExecuteService.votePlace(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot.getId() + 1L)));
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

        List<PlaceVote> placeVotes = voteExecuteService.votePlace(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot1.getId(), savePlaceSlot2.getId()));

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
        assertThrows(NotFoundParticipantException.class, () -> voteExecuteService.votePlace(saveUser.getId(), saveMeet.getId(), Set.of(savePlaceSlot1.getId(), savePlaceSlot2.getId())));
        System.out.println("=====Logic End=====");
    }
}