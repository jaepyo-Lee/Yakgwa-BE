package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    @AfterEach
    void init() {
        timeSlotJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
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
        // when
        System.out.println("=====Logic Start=====");

        timeVoteWriter.confirmAndWrite(saveMeet, confirmTime);

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

        // when
        System.out.println("=====Logic Start=====");

        timeVoteWriter.confirmAndWrite(saveMeet, null);

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
}