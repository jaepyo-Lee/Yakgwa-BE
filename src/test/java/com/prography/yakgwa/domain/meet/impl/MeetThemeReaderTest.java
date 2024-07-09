package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class MeetThemeReaderTest {

    @Autowired
    MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    MeetThemeReader meetThemeReader;

    @BeforeEach
    void init() {
        meetThemeJpaRepository.deleteAll();
    }

    @Test
    void 테마_전체조회Test() {
        // given
        MeetTheme meetTheme1 = MeetTheme.builder().id(1l).name("테마1").build();
        MeetTheme meetTheme2 = MeetTheme.builder().id(2l).name("테마2").build();
        meetThemeJpaRepository.save(meetTheme1);
        meetThemeJpaRepository.save(meetTheme2);
        // when
        System.out.println("=====Business Logic=====");
        List<MeetTheme> meetThemes = meetThemeReader.readAll();
        // then
        assertThat(meetThemes.size()).isEqualTo(2);

    }

    @Test
    void 모임테마의_pk값으로_조회하는Test() {
        // given
        MeetTheme meetTheme1 = MeetTheme.builder().id(1l).name("테마1").build();
        MeetTheme meetTheme2 = MeetTheme.builder().id(2l).name("테마2").build();
        MeetTheme save1 = meetThemeJpaRepository.save(meetTheme1);
        MeetTheme save2 = meetThemeJpaRepository.save(meetTheme2);

        // when
        System.out.println("=====Business Logic=====");
        MeetTheme meetTheme = meetThemeReader.read(save1.getId());
        // then
        assertThat(meetTheme.getId()).isEqualTo(save1.getId());
    }

    @Test
    void 존재하지않는_객체를_조회할때_예외Test() {
        // given
        MeetTheme meetTheme1 = MeetTheme.builder().id(1l).name("테마1").build();
        MeetTheme save1 = meetThemeJpaRepository.save(meetTheme1);
        // when
        // then
        System.out.println("=====Business Logic=====");
        assertThrows(RuntimeException.class, () -> meetThemeReader.read(save1.getId() + 1L));
    }
}