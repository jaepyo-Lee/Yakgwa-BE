package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.common.DummyCreater;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class MeetThemeServiceInteTest {
    @Autowired
    MeetThemeService meetThemeService;
    @Autowired
    MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    DummyCreater dummyCreater;
    @AfterEach
    void init(){
        meetThemeJpaRepository.deleteAll();
    }

    @Test
    void 모임의테마_조회() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);

        // when
        System.out.println("=====Logic Start=====");

        List<MeetTheme> meetThemes = meetThemeService.getMeetThemes();

        System.out.println("=====Logic End=====");
        // then
        assertThat(meetThemes.size()).isEqualTo(1);
    }
}