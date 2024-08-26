package com.prography.yakgwa.domain.participant.impl;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ParticipantReaderTest extends IntegrationTestSupport {


    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 모임내의참가자Impl_전체조회Test() {
        // given
        User user1 = User.builder()
                .name("user1").authId("authId1").authType(KAKAO)
                .build();
        User user2 = User.builder()
                .name("user2").authId("authId2").authType(KAKAO)
                .build();
        User saveUser1 = userJpaRepository.save(user1);
        User saveUser2 = userJpaRepository.save(user2);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet meet = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        Meet saveMeet = meetJpaRepository.save(meet);

        Participant participant1 = Participant.builder().meetRole(MeetRole.LEADER).meet(saveMeet).user(saveUser1).build();
        Participant participant2 = Participant.builder().meetRole(MeetRole.PARTICIPANT).meet(saveMeet).user(saveUser2).build();
        participantJpaRepository.save(participant1);
        participantJpaRepository.save(participant2);

        // when
        System.out.println("=====Logic Start=====");

        List<Participant> allByMeetId = participantJpaRepository.findAllByMeetId(saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(allByMeetId.size()).isEqualTo(2);
    }

    @Test
    void 사용자의참가자Impl_전체조회Test() {
        // given
        User user1 = User.builder()
                .name("user1").authId("authId1").authType(KAKAO)
                .build();
        User saveUser1 = userJpaRepository.save(user1);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet meet1 = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        Meet meet2 = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        Meet meet3 = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        Meet meet4 = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();

        Meet saveMeet1 = meetJpaRepository.save(meet1);
        Meet saveMeet2 = meetJpaRepository.save(meet2);
        Meet saveMeet3 = meetJpaRepository.save(meet3);
        Meet saveMeet4 = meetJpaRepository.save(meet4);

        Participant participant1 = Participant.builder().meetRole(MeetRole.LEADER).meet(saveMeet1).user(saveUser1).build();
        Participant participant2 = Participant.builder().meetRole(MeetRole.LEADER).meet(saveMeet2).user(saveUser1).build();
        Participant participant3 = Participant.builder().meetRole(MeetRole.LEADER).meet(saveMeet3).user(saveUser1).build();
        Participant participant4 = Participant.builder().meetRole(MeetRole.LEADER).meet(saveMeet4).user(saveUser1).build();

        participantJpaRepository.save(participant1);
        participantJpaRepository.save(participant2);
        participantJpaRepository.save(participant3);
        participantJpaRepository.save(participant4);

        // when
        System.out.println("=====Logic Start=====");

        List<Participant> allByMeetId = participantJpaRepository.findAllByUserId(saveUser1.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(allByMeetId.size()).isEqualTo(4);
    }

    @Test
    void 특정모임에사용자가참여중이아닐때Impl_참가정보조회_Test() {
        // given
        User user1 = User.builder()
                .name("user1").authId("authId1").authType(KAKAO)
                .build();
        User saveUser1 = userJpaRepository.save(user1);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet meet1 = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        Meet saveMeet1 = meetJpaRepository.save(meet1);

        // when
        System.out.println("=====Logic Start=====");

        assertThrows(RuntimeException.class, () -> participantJpaRepository.findByUserIdAndMeetId(saveUser1.getId(), saveMeet1.getId())
                .orElseThrow(NotFoundParticipantException::new));

        System.out.println("=====Logic End=====");

        // then
    }

    @Test
    void 특정모임에사용자가참여중일때Impl_참가정보조회_Test() {
        // given
        User user1 = User.builder()
                .name("user1").authId("authId1").authType(KAKAO)
                .build();
        User saveUser1 = userJpaRepository.save(user1);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet meet1 = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        Meet saveMeet1 = meetJpaRepository.save(meet1);

        Participant participant1 = Participant.builder().meetRole(MeetRole.LEADER).meet(saveMeet1).user(saveUser1).build();
        Participant saveParticipant = participantJpaRepository.save(participant1);

        // when
        System.out.println("=====Logic Start=====");

        Participant participant = participantJpaRepository.findByUserIdAndMeetId(saveUser1.getId(), saveMeet1.getId())
                .orElseThrow(NotFoundParticipantException::new);

        System.out.println("=====Logic End=====");

        // then
        assertAll(() -> assertThat(participant.getNickname()).isEqualTo(saveParticipant.getNickname()),
                () -> assertThat(participant.getMeetRole()).isEqualTo(saveParticipant.getMeetRole()),
                () -> assertThat(participant.getId()).isEqualTo(saveParticipant.getId()),
                () -> assertThat(participant.getUser().getId()).isEqualTo(saveParticipant.getUser().getId()),
                () -> assertThat(participant.getMeet().getId()).isEqualTo(saveParticipant.getMeet().getId()));

    }
}

