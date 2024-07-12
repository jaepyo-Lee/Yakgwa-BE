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
import com.prography.yakgwa.domain.vote.impl.dto.ConfirmTimeDto;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class TimeVoteWriterTest {
    @Autowired
    private MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    private MeetJpaRepository meetJpaRepository;
    @Autowired
    TimeVoteWriter timeVoteWriter;
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
    void 시간확정되었을때_시간후보등록() {
        // given
        MeetTheme theme = MeetTheme.builder()
                .name("데이트")
                .build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);

        Meet saveMeet = createAndSaveMeet(1, saveTheme);

        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime confirmTime = LocalDateTime.parse(formattedDate + "T00:00:00");

        ConfirmTimeDto confirmTimeDto = ConfirmTimeDto.builder().meetTime(confirmTime).build();
        // when
        System.out.println("=====Logic Start=====");

        timeVoteWriter.confirmAndWrite(saveMeet, confirmTimeDto);

        System.out.println("=====Logic End=====");
        // then

        List<TimeSlot> all = timeSlotJpaRepository.findAll();
        assertAll(() -> assertThat(all.size()).isEqualTo(1),
                () -> assertThat(all.get(0).getTime()).isEqualTo(confirmTime),
                () -> assertThat(all.get(0).getMeet().getId()).isEqualTo(saveMeet.getId()));

    }

    @Test
    void 시간확정되지않았을때_시간후보등록() {
        // given
        MeetTheme theme = MeetTheme.builder()
                .name("데이트")
                .build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);

        Meet saveMeet = createAndSaveMeet(1, saveTheme);

        ConfirmTimeDto confirmTimeDto = ConfirmTimeDto.builder().meetTime(null).build();
        // when
        System.out.println("=====Logic Start=====");

        timeVoteWriter.confirmAndWrite(saveMeet, confirmTimeDto);

        System.out.println("=====Logic End=====");
        // then

        List<TimeSlot> all = timeSlotJpaRepository.findAll();
        assertAll(() -> assertThat(all.size()).isEqualTo(0));

    }

    private Meet createAndSaveMeet(int idx, MeetTheme saveTheme) {
        Meet meet = Meet.builder()
                .title("title" + idx).validInviteHour(24).meetTheme(saveTheme).period(new VotePeriod(now(), now().plusDays(1L)))
                .build();
        return meetJpaRepository.save(meet);
    }

    @Test
    void 시간투표_생성() {
        // given
        User saveUser = createAndSaveUser(1L);
        MeetTheme theme = MeetTheme.builder().name("theme").build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);
        Meet saveMeet = createAndSaveMeet(1, saveTheme);
        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet,LocalDateTime.now());
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet,LocalDateTime.now().plusDays(1L));
        TimeSlot saveTimeSlot3 = createAndSaveTimeSlot(saveMeet,LocalDateTime.now().plusDays(2L));
        TimeSlot saveTimeSlot4 = createAndSaveTimeSlot(saveMeet,LocalDateTime.now().plusDays(3L));

        // when
        System.out.println("=====Logic Start=====");

        timeVoteWriter.writeAll(saveUser, List.of(saveTimeSlot1, saveTimeSlot2, saveTimeSlot3, saveTimeSlot4));

        System.out.println("=====Logic End=====");
        // then
        List<TimeVote> all = timeVoteJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(4);
    }

    @Test
    void 모임에투표한시간내역_특정모임의본인투표만전체삭제() {
        // given
        User saveUser = createAndSaveUser(1L);
        User differentUser = createAndSaveUser(2L);

        MeetTheme theme = MeetTheme.builder().name("theme").build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);
        Meet saveMeet = createAndSaveMeet(1, saveTheme);
        Meet compareMeet = createAndSaveMeet(1, saveTheme);

        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet,LocalDateTime.now());
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet,LocalDateTime.now().plusDays(1L));
        TimeSlot saveTimeSlot3 = createAndSaveTimeSlot(saveMeet,LocalDateTime.now().plusDays(2L));
        TimeSlot saveTimeSlot4 = createAndSaveTimeSlot(saveMeet,LocalDateTime.now().plusDays(3L));
        createAndSaveTimeVote(saveUser,saveTimeSlot1);
        createAndSaveTimeVote(saveUser,saveTimeSlot2);
        createAndSaveTimeVote(saveUser,saveTimeSlot3);
        createAndSaveTimeVote(saveUser,saveTimeSlot4);

        TimeSlot compareTimeSlot1 = createAndSaveTimeSlot(compareMeet,LocalDateTime.now());
        TimeSlot compareTimeSlot2 = createAndSaveTimeSlot(compareMeet,LocalDateTime.now().plusDays(1L));
        createAndSaveTimeVote(saveUser,compareTimeSlot1);
        createAndSaveTimeVote(saveUser,compareTimeSlot2);

        TimeSlot differentTimeSlot1 = createAndSaveTimeSlot(compareMeet,LocalDateTime.now());
        createAndSaveTimeVote(differentUser,differentTimeSlot1);

        TimeSlot differentTimeSlot2 = createAndSaveTimeSlot(saveMeet,LocalDateTime.now());
        createAndSaveTimeVote(differentUser,differentTimeSlot2);

        // when
        System.out.println("=====Logic Start=====");

        timeVoteWriter.deleteAllVoteOfUser(saveUser, saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        List<TimeVote> allTimeVoteBySaveMeet = timeVoteJpaRepository.findAllByMeetId(saveMeet.getId());
        List<TimeVote> allTimeVoteByCompareMeet = timeVoteJpaRepository.findAllByMeetId(compareMeet.getId());
        assertAll(()-> assertThat(allTimeVoteBySaveMeet.size()).isEqualTo(1),
                ()-> assertThat(allTimeVoteByCompareMeet.size()).isEqualTo(3));

    }

    private TimeVote createAndSaveTimeVote(User user,TimeSlot timeSlot) {
        TimeVote timeVote = TimeVote.builder()
                .timeSlot(timeSlot).user(user)
                .build();
        return timeVoteJpaRepository.save(timeVote);
    }


    private TimeSlot createAndSaveTimeSlot(Meet saveMeet,LocalDateTime time) {
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
}