package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class TimeVoteReaderTest {
    @Autowired
    private MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    private MeetJpaRepository meetJpaRepository;
    @Autowired
    TimeVoteReader timeVoteReader;
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private TimeVoteJpaRepository timeVoteJpaRepository;

    @AfterEach
    void init() {
        timeVoteJpaRepository.deleteAll();
        timeSlotJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @Test
    void 모임내에사용자의시간투표했을때_투표여부확인() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("themt").build());
        Meet saveMeet = createAndSaveMeet(1, theme);
        User saveUser = createAndSaveUser(1L);

        TimeSlot saveTimeSlot = createAndSaveTimeSlot(saveMeet, LocalDateTime.now());
        TimeVote saveTimeVote = createAndSaveTimeVote(saveUser, saveTimeSlot);


        // when
        System.out.println("=====Logic Start=====");

        boolean isVoteTIme = timeVoteReader.existsByUserIdInMeet(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(isVoteTIme).isTrue();

    }

    @Test
    void 모임내에사용자의시간안했을때_투표여부확인() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("themt").build());
        Meet saveMeet = createAndSaveMeet(1, theme);
        User saveUser = createAndSaveUser(1L);

        TimeSlot saveTimeSlot = createAndSaveTimeSlot(saveMeet, LocalDateTime.now());

        // when
        System.out.println("=====Logic Start=====");

        boolean isVoteTIme = timeVoteReader.existsByUserIdInMeet(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(isVoteTIme).isFalse();

    }

    @Test
    void 다른모임에만시간투표했을때_특정모임의투표여부확인() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("themt").build());
        User saveUser = createAndSaveUser(1L);

        Meet saveMeet = createAndSaveMeet(1, theme);

        Meet compareMeet = createAndSaveMeet(2, theme);
        TimeSlot compareTimeSlot = createAndSaveTimeSlot(compareMeet, LocalDateTime.now());

        createAndSaveTimeVote(saveUser, compareTimeSlot);

        // when
        System.out.println("=====Logic Start=====");

        boolean isVoteTIme = timeVoteReader.existsByUserIdInMeet(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(isVoteTIme).isFalse();

    }

    @Test
    void 모임에서사용자가투표한내역_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("themt").build());
        User saveUser = createAndSaveUser(1L);

        Meet saveMeet = createAndSaveMeet(1, theme);
        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now());
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now().plusDays(1L));

        TimeVote saveTimeVote1 = createAndSaveTimeVote(saveUser, saveTimeSlot1); //투표
        TimeVote saveTimeVote2 = createAndSaveTimeVote(saveUser, saveTimeSlot2); //투표

        // when
        System.out.println("=====Logic Start=====");

        List<TimeVote> timeVoteOfUserInMeet = timeVoteReader.findAllTimeVoteOfUserInMeet(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(timeVoteOfUserInMeet.size()).isEqualTo(2);
    }

    @Test
    void 다른모임에도투표했을때_특정모임에서사용자가투표한내역_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("themt").build());
        User saveUser = createAndSaveUser(1L);
        User compareUser = createAndSaveUser(2L);

        Meet saveMeet = createAndSaveMeet(1, theme);
        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now());
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now().plusDays(1L));

        TimeVote saveTimeVote1 = createAndSaveTimeVote(saveUser, saveTimeSlot1); //투표
        TimeVote saveTimeVote2 = createAndSaveTimeVote(saveUser, saveTimeSlot2); //투표
        TimeVote saveTimeVote3 = createAndSaveTimeVote(compareUser, saveTimeSlot2); //투표

        Meet compareMeet = createAndSaveMeet(2, theme);
        TimeSlot compareTimeSlot1 = createAndSaveTimeSlot(compareMeet, LocalDateTime.now());
        TimeSlot compareTimeSlot2 = createAndSaveTimeSlot(compareMeet, LocalDateTime.now().plusDays(1L));

        TimeVote compareTimeVote1 = createAndSaveTimeVote(saveUser, compareTimeSlot1); //비교투표
        TimeVote compareTimeVote2 = createAndSaveTimeVote(saveUser, compareTimeSlot2); //비교투표

        // when
        System.out.println("=====Logic Start=====");

        List<TimeVote> timeVoteOfUserInMeet = timeVoteReader.findAllTimeVoteOfUserInMeet(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(timeVoteOfUserInMeet.size()).isEqualTo(2);
    }

    private TimeVote createAndSaveTimeVote(User saveUser, TimeSlot saveTimeSlot) {
        TimeVote timeVote = TimeVote.builder()
                .user(saveUser).timeSlot(saveTimeSlot)
                .build();
        return timeVoteJpaRepository.save(timeVote);
    }

    private TimeSlot createAndSaveTimeSlot(Meet saveMeet, LocalDateTime time) {
        TimeSlot timeSlot = TimeSlot.builder()
                .time(time).confirm(false).meet(saveMeet)
                .build();
        return timeSlotJpaRepository.save(timeSlot);
    }

    private User createAndSaveUser(Long id) {
        User user = User.builder()
                .name("name" + id).imageUrl("imageUrl" + id).fcmToken("fcmtoken" + id).authId("authId" + id).authType(AuthType.KAKAO).isNew(true)
                .build();
        return userJpaRepository.save(user);
    }
    private Meet createAndSaveMeet(int idx, MeetTheme saveTheme) {
        Meet meet = Meet.builder()
                .title("title" + idx).validInviteHour(24).meetTheme(saveTheme).period(new VotePeriod(now(), now().plusDays(1L)))
                .build();
        return meetJpaRepository.save(meet);
    }
}