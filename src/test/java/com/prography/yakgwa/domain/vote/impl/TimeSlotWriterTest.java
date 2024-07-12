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
import org.springframework.cglib.core.Local;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class TimeSlotWriterTest {


    @Autowired
    private MeetJpaRepository meetJpaRepository;
    @Autowired
    private MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    private TimeSlotWriter timeSlotWriter;

    @AfterEach
    void init(){
        timeSlotJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
    }

    @Test
    void 시간투표후보지_전체저장() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme);
        List<LocalDateTime> times = List.of(now(), now().plusDays(1L), now().plusDays(2L), now().plusDays(3L));

        // when
        System.out.println("=====Logic Start=====");

        timeSlotWriter.writeAll(saveMeet, times);

        System.out.println("=====Logic End=====");
        // then
        List<TimeSlot> all = timeSlotJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(4);
    }

    private Meet createAndSaveMeet(Long id, MeetTheme saveMeetTheme) {
        Meet meet = Meet.builder()
                .title("title" + id).validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        return meetJpaRepository.save(meet);
    }
}