package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class MeetReaderTest {
    @Autowired
    MeetJpaRepository meetJpaRepository;
    @Autowired
    MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    MeetReader meetReader;

    @AfterEach
    void inti() {
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
    }

    @Transactional
    @Test
    void 모임조회Test() {
        // given
        MeetTheme theme = MeetTheme.builder().id(1l).name("test").build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);
        Meet meet = Meet.builder().period(VotePeriod.builder()
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1l))
                        .build())
                .meetTheme(saveTheme)
                .title("title")
                .validInviteHour(24)
                .build();
        Meet saveMeet = meetJpaRepository.save(meet);
        // when
        Meet compareMeet = meetReader.read(saveMeet.getId());
        // then
        Assertions.assertAll(() -> assertEquals(saveMeet.getTitle(), compareMeet.getTitle()),
                () -> assertEquals(saveMeet.getId(), compareMeet.getId()),
                () -> assertEquals(saveMeet.getValidInviteHour(), compareMeet.getValidInviteHour()),
                () -> assertEquals(saveMeet.getPeriod().getStartDate(), compareMeet.getPeriod().getStartDate()),
                () -> assertEquals(saveMeet.getPeriod().getEndDate(), compareMeet.getPeriod().getEndDate()),
                () -> assertEquals(saveMeet.getMeetTheme().getName(), compareMeet.getMeetTheme().getName()));
    }

    @Test
    void 모임_조회시_존재하지않을경우Test() {
        // given
        Long notExistMeetId = 1l;
        // when
        // then
        Assertions.assertThrows(NotFoundMeetException.class, () -> meetReader.read(notExistMeetId));
    }
}