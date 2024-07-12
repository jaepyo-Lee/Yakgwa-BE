package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class TimeSlotReaderTest {
    @Autowired
    private MeetJpaRepository meetJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    private TimeSlotReader timeSlotReader;

    @AfterEach
    void init() {
        timeSlotJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @Test
    void 시간후보중확정되었을때_확정값조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = now.format(formatter);
        LocalDateTime time = LocalDateTime.parse(formattedDate + "T00:00:00");

        TimeSlot saveTimeSlot = createAndSaveTimeSlot(saveMeet, time, true);
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, time, false);

        // when
        System.out.println("=====Logic Start=====");

        TimeSlot timeSlot = timeSlotReader.readConfirmOrNullByMeetId(saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(timeSlot).isNotNull(),
                () -> assertThat(timeSlot.getId()).isEqualTo(saveTimeSlot.getId())
                , () -> assertThat(timeSlot.getTime()).isEqualTo(time));
    }

    @Test
    void 시간후보중확정된것이없을때_조회시null반환() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = now.format(formatter);
        LocalDateTime time = LocalDateTime.parse(formattedDate + "T00:00:00");

        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet, time, false);
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, time, false);

        // when
        System.out.println("=====Logic Start=====");

        TimeSlot timeSlot = timeSlotReader.readConfirmOrNullByMeetId(saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(timeSlot).isNull());
    }

    @Test
    void 특정시간후보조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = now.format(formatter);
        LocalDateTime time = LocalDateTime.parse(formattedDate + "T00:00:00");

        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet, time, false);
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, time, false);

        // when
        System.out.println("=====Logic Start=====");

        TimeSlot timeSlot = timeSlotReader.read(saveTimeSlot1.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(timeSlot.getId()).isEqualTo(saveTimeSlot1.getId()),
                () -> assertThat(timeSlot.getTime()).isEqualTo(saveTimeSlot1.getTime()),
                () -> assertThat(timeSlot.getMeet().getId()).isEqualTo(saveTimeSlot1.getMeet().getId()));
    }

    @Test
    void 특정시간후보조회시_후보없을때예외() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = now.format(formatter);
        LocalDateTime time = LocalDateTime.parse(formattedDate + "T00:00:00");

        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet, time, false);
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, time, false);

        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThrows(RuntimeException.class, () -> timeSlotReader.read(saveTimeSlot2.getId() + 1L));

        System.out.println("=====Logic End=====");
    }

    private TimeSlot createAndSaveTimeSlot(Meet saveMeet, LocalDateTime time, boolean confirm) {
        return timeSlotJpaRepository.save(TimeSlot.builder().meet(saveMeet).time(time).confirm(confirm).build());
    }

    private Meet createAndSaveMeet(Long id, MeetTheme saveMeetTheme) {
        Meet meet = Meet.builder()
                .title("title" + id).validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        return meetJpaRepository.save(meet);
    }

    private User createAndSaveUser(Long id) {
        User user = User.builder()
                .name("user" + id).isNew(true).authId("authId1").authType(KAKAO)
                .build();
        return userJpaRepository.save(user);
    }

}