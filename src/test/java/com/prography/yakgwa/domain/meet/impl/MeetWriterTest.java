package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.impl.dto.MeetWriteDto;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class MeetWriterTest {
    @Autowired
    MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    MeetJpaRepository meetJpaRepository;
    @Autowired
    MeetWriter meetWriter;
    @BeforeEach
    void init() {
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
    }

    @Test
    void 투표일때_모임생성Test() {
        // given
        MeetTheme meetTheme = new MeetTheme(1L, "데이트");
        meetThemeJpaRepository.save(meetTheme);
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1l);
        String title = "test";
        MeetWriteDto writeDto = MeetWriteDto.builder()
                .meetTime(null)
                .period(new VotePeriod(from,to))
                .title(title)
                .meetThemeId(meetTheme.getId())
                .build();
        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetWriter.write(writeDto);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(meet.getTitle()).isEqualTo(title),
                () -> assertThat(meet.getValidInviteHour()).isEqualTo(24),
                () -> assertThat(meet.getMeetTheme().getName()).isEqualTo(meetTheme.getName()),
                ()-> assertThat(meet.getPeriod().getEndDate()).isEqualTo(to),
                ()-> assertThat(meet.getPeriod().getStartDate()).isEqualTo(from));
    }

    @Test
    void 시간확정일때_모임생성Test() {
        // given
        MeetTheme meetTheme = new MeetTheme(1L, "데이트");
        meetThemeJpaRepository.save(meetTheme);
        LocalDateTime time = LocalDateTime.now();
        String title = "test";
        MeetWriteDto writeDto = MeetWriteDto.builder()
                .meetTime(time)
                .period(null)
                .title(title)
                .meetThemeId(meetTheme.getId())
                .build();

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetWriter.write(writeDto);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(meet.getTitle()).isEqualTo(title),
                () -> assertThat(meet.getValidInviteHour()).isEqualTo(24),
                () -> assertThat(meet.getMeetTheme().getName()).isEqualTo(meetTheme.getName()),
                ()-> assertThat(meet.getPeriod()).isNull());
    }

    @Test
    void 모임생성시_시간관련파라미터가_모두NULL인경우_예외Test() {
        // given
        MeetTheme meetTheme = new MeetTheme(1L, "데이트");
        meetThemeJpaRepository.save(meetTheme);
        String title = "test";
        MeetWriteDto writeDto = MeetWriteDto.builder()
                .meetTime(null)
                .period(null)
                .title(title)
                .meetThemeId(meetTheme.getId())
                .build();

        // when
        System.out.println("=====Logic Start=====");

        assertThrows(RuntimeException.class,()->meetWriter.write(writeDto));

        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 모임생성시_시간관련파라미터가_모두들어가있는경우_예외Test() {
        // given
        MeetTheme meetTheme = new MeetTheme(1L, "데이트");
        meetThemeJpaRepository.save(meetTheme);
        LocalDateTime time = LocalDateTime.now();
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1l);
        String title = "test";
        MeetWriteDto writeDto = MeetWriteDto.builder()
                .meetTime(time)
                .period(new VotePeriod(from,to))
                .title(title)
                .meetThemeId(meetTheme.getId())
                .build();

        // when
        System.out.println("=====Logic Start=====");

        assertThrows(RuntimeException.class,()->meetWriter.write(writeDto));

        System.out.println("=====Logic End=====");
        // then
    }
}