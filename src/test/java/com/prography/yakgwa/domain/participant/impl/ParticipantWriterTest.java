package com.prography.yakgwa.domain.participant.impl;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole.LEADER;
import static com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole.PARTICIPANT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParticipantWriterTest extends IntegrationTestSupport {


    @AfterEach
    void init() {
    deleter.deleteAll();
    }

    @Test
    void 리더로_참가하기Test() {
        // given
        User user = User.builder()
                .name("name").isNew(true).authId("authId").authType(AuthType.KAKAO).fcmToken("fcmtoken").imageUrl("imageurl")
                .build();
        User saveUser = userJpaRepository.save(user);


        MeetTheme theme = MeetTheme.builder()
                .name("name")
                .build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);

        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1l);
        Meet meet = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(from, to)).meetTheme(saveTheme)
                .build();
        Meet saveMeet = meetJpaRepository.save(meet);

        // when
        System.out.println("=====Logic Start=====");

        Participant participant = participantWriter.registLeader(saveMeet, saveUser);

        System.out.println("=====Logic End=====");
        // then

        assertThat(participant.getMeetRole()).isEqualTo(LEADER);
    }

    @Test
    void 참가자로_참가하기Test() {
        // given
        User user = User.builder()
                .name("name").isNew(true).authId("authId").authType(AuthType.KAKAO).fcmToken("fcmtoken").imageUrl("imageurl")
                .build();
        User saveUser = userJpaRepository.save(user);


        MeetTheme theme = MeetTheme.builder()
                .name("name")
                .build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);

        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1l);
        Meet meet = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(from, to)).meetTheme(saveTheme)
                .build();
        Meet saveMeet = meetJpaRepository.save(meet);

        // when
        System.out.println("=====Logic Start=====");

        Participant participant = participantWriter.registParticipant(saveMeet, saveUser);

        System.out.println("=====Logic End=====");
        // then

        assertThat(participant.getMeetRole()).isEqualTo(PARTICIPANT);
    }

    @Test
    void 이미참여중인모임_참가했을때_예외Test() {
        // given
        User user = User.builder()
                .name("name").isNew(true).authId("authId").authType(AuthType.KAKAO).fcmToken("fcmtoken").imageUrl("imageurl")
                .build();
        User saveUser = userJpaRepository.save(user);


        MeetTheme theme = MeetTheme.builder()
                .name("name")
                .build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);

        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1l);
        Meet meet = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(from, to)).meetTheme(saveTheme)
                .build();
        Meet saveMeet = meetJpaRepository.save(meet);

        Participant participant = Participant.builder().user(saveUser).meet(saveMeet).meetRole(LEADER).build();
        participantJpaRepository.save(participant);

        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThrows(RuntimeException.class, () -> participantWriter.registParticipant(saveMeet, saveUser));

        System.out.println("=====Logic End=====");
    }
}