package com.prography.yakgwa.domain.vote.service.time;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.service.time.req.EnableTimeRequestDto;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import com.prography.yakgwa.global.format.exception.vote.NotValidMeetVoteDateException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TimeVoteExecuteServiceTest extends IntegrationTestSupport {

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

        List<TimeVote> timeVotes = voteExecuter.vote(saveUser.getId(), saveMeet.getId(), request);

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

        List<TimeVote> timeVotes = voteExecuter.vote(saveUser.getId(), saveMeet.getId(), request);

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

        List<TimeVote> timeVotes = voteExecuter.vote(saveUser.getId(), saveMeet.getId(), request);

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
        assertThrows(NotFoundParticipantException.class, () -> voteExecuter.vote(saveUser.getId(), saveMeet.getId(), request));
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
        assertThrows(NotValidMeetVoteDateException.class, () -> voteExecuter.vote(saveUser.getId(), saveMeet.getId(), request));
    }

}