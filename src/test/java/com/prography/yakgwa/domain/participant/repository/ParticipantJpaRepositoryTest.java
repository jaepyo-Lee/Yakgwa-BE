package com.prography.yakgwa.domain.participant.repository;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.testHelper.config.DeleterConfig;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

class ParticipantJpaRepositoryTest extends IntegrationTestSupport {
    @Autowired
    MeetJpaRepository meetJpaRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    ParticipantJpaRepository participantJpaRepository;
    @Autowired
    MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    RepositoryDeleter deleter;
    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 모임에속한참여자_전체조회Test() {
        // given
        User user1 = User.builder()
                .name("user1").isNew(true).authId("authId1").authType(KAKAO)
                .build();
        User user2 = User.builder()
                .name("user2").isNew(true).authId("authId2").authType(KAKAO)
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
    void 사용자의참가자_전체조회Test() {
        // given
        User user1 = User.builder()
                .name("user1").isNew(true).authId("authId1").authType(KAKAO)
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

        // when
        System.out.println("=====Logic Start=====");


        System.out.println("=====Logic End=====");
        // then
    }


    @Test
    void 특정모임에사용자가참여중일때_참가여부조회_Test() {
        // given
        User user1 = User.builder()
                .name("user1").isNew(true).authId("authId1").authType(KAKAO)
                .build();
        User saveUser1 = userJpaRepository.save(user1);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet meet1 = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        Meet saveMeet1 = meetJpaRepository.save(meet1);

        Participant participant1 = Participant.builder().meetRole(MeetRole.LEADER).meet(saveMeet1).user(saveUser1).build();

        participantJpaRepository.save(participant1);

        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThat(participantJpaRepository.existsByUserIdAndMeetId(saveUser1.getId(), saveMeet1.getId())).isTrue();

        System.out.println("=====Logic End=====");
    }

    @Test
    void 특정모임에사용자가참여중이아닐때_참가여부조회_Test() {
        // given
        User user1 = User.builder()
                .name("user1").isNew(true).authId("authId1").authType(KAKAO)
                .build();
        User saveUser1 = userJpaRepository.save(user1);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet meet1 = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        Meet saveMeet1 = meetJpaRepository.save(meet1);

        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThat(participantJpaRepository.existsByUserIdAndMeetId(saveUser1.getId(), saveMeet1.getId())).isFalse();

        System.out.println("=====Logic End=====");
    }

    @Test
    void 특정모임에사용자가참여중이아닐때_참가정보조회_Test() {
        // given
        User user1 = User.builder()
                .name("user1").isNew(true).authId("authId1").authType(KAKAO)
                .build();
        User saveUser1 = userJpaRepository.save(user1);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet meet1 = Meet.builder()
                .title("title").validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        Meet saveMeet1 = meetJpaRepository.save(meet1);

        Participant participant1 = Participant.builder().meetRole(MeetRole.LEADER).meet(saveMeet1).user(saveUser1).build();

        participantJpaRepository.save(participant1);

        // when
        System.out.println("=====Logic Start=====");

        Optional<Participant> byUserIdAndMeetId = participantJpaRepository.findByUserIdAndMeetId(saveUser1.getId(), saveMeet1.getId());

        System.out.println("=====Logic End=====");

        // then
        assertThat(byUserIdAndMeetId).isPresent();
    }

    @Test
    void 특정모임에사용자가참여중일때_참가정보조회_Test() {
        // given
        User user1 = User.builder()
                .name("user1").isNew(true).authId("authId1").authType(KAKAO)
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
        Optional<Participant> byUserIdAndMeetId = participantJpaRepository.findByUserIdAndMeetId(saveUser1.getId(), saveMeet1.getId());

        System.out.println("=====Logic End=====");

        // then
        assertThat(byUserIdAndMeetId).isEmpty();
    }
}