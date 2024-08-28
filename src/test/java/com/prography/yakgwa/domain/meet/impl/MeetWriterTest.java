package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.impl.dto.MeetWriteDto;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.meet.impl.dto.ConfirmPlaceDto;
import com.prography.yakgwa.domain.meet.impl.dto.ConfirmTimeDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MeetWriterTest extends IntegrationTestSupport{

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 투표일때_모임생성Test() {
        // given
        MeetTheme meetTheme = new MeetTheme(1L, "데이트");
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(meetTheme);
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1l);
        String title = "test";
        MeetWriteDto writeDto = MeetWriteDto.builder()
                .meetTime(null)
                .period(new VotePeriod(from, to))
                .title(title)
                .meetThemeId(saveMeetTheme.getId())
                .build();

        PlaceInfoDto placeInfoDto1 = PlaceInfoDto.builder().title("title").build();
        PlaceInfoDto placeInfoDto2 = PlaceInfoDto.builder().title("title").build();

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetWriter.write(writeDto);

        System.out.println("=====Logic End=====");

        // then
        assertAll(() -> assertThat(meet.getTitle()).isEqualTo(title),
                () -> assertThat(meet.getValidInviteHour()).isEqualTo(24),
                () -> assertThat(meet.getThemeName()).isEqualTo(meetTheme.getName()),
                () -> assertThat(meet.getVoteEndDate()).isEqualTo(to),
                () -> assertThat(meet.getVoteStartDate()).isEqualTo(from));
    }

    @Test
    void 시간확정일때_모임생성Test() {
        // given
        MeetTheme meetTheme = new MeetTheme(1L, "데이트");
        MeetTheme savevMeetTheme = meetThemeJpaRepository.save(meetTheme);
        LocalDateTime time = LocalDateTime.now();
        String title = "test";
        MeetWriteDto writeDto = MeetWriteDto.builder()
                .meetTime(time)
                .period(null)
                .title(title)
                .meetThemeId(savevMeetTheme.getId())
                .build();

        PlaceInfoDto placeInfoDto1 = PlaceInfoDto.builder().title("title").build();
        PlaceInfoDto placeInfoDto2 = PlaceInfoDto.builder().title("title").build();

        ConfirmPlaceDto confirmPlaceDto = ConfirmPlaceDto.builder()
                .placeInfo(List.of(placeInfoDto1, placeInfoDto2)).confirmPlace(false)
                .build();

        ConfirmTimeDto confirmTimeDto = ConfirmTimeDto.builder()
                .meetTime(LocalDateTime.now())
                .build();

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetWriter.write(writeDto);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(meet.getTitle()).isEqualTo(title),
                () -> assertThat(meet.getValidInviteHour()).isEqualTo(24),
                () -> assertThat(meet.getMeetTheme().getName()).isEqualTo(meetTheme.getName()),
                () -> assertThat(meet.getPeriod()).isNull());
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


        PlaceInfoDto placeInfoDto1 = PlaceInfoDto.builder().title("title").build();
        PlaceInfoDto placeInfoDto2 = PlaceInfoDto.builder().title("title").build();

        ConfirmPlaceDto confirmPlaceDto = ConfirmPlaceDto.builder()
                .placeInfo(List.of(placeInfoDto1, placeInfoDto2)).confirmPlace(false)
                .build();

        ConfirmTimeDto confirmTimeDto = ConfirmTimeDto.builder()
                .meetTime(null)
                .build();


        // when
        System.out.println("=====Logic Start=====");

        assertThrows(RuntimeException.class, () -> meetWriter.write(writeDto));

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
                .period(new VotePeriod(from, to))
                .title(title)
                .meetThemeId(meetTheme.getId())
                .build();


        PlaceInfoDto placeInfoDto1 = PlaceInfoDto.builder().title("title").build();
        PlaceInfoDto placeInfoDto2 = PlaceInfoDto.builder().title("title").build();

        ConfirmPlaceDto confirmPlaceDto = ConfirmPlaceDto.builder()
                .placeInfo(List.of(placeInfoDto1, placeInfoDto2)).confirmPlace(false)
                .build();

        ConfirmTimeDto confirmTimeDto = ConfirmTimeDto.builder()
                .meetTime(null)
                .build();


        // when
        System.out.println("=====Logic Start=====");

        assertThrows(RuntimeException.class, () -> meetWriter.write(writeDto));

        System.out.println("=====Logic End=====");
        // then
    }
}