package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.testHelper.config.DeleterConfig;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(DeleterConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
class TimeSlotJpaRepositoryTest {
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    private MeetJpaRepository meetJpaRepository;
    @Autowired
    private MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    RepositoryDeleter deleter;
    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 모임에대한모든시간후보지_전체조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme);
        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);

        // when
        System.out.println("=====Logic Start=====");

        List<TimeSlot> allByMeetId = timeSlotJpaRepository.findAllByMeetId(saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(allByMeetId.size()).isEqualTo(2);

    }

    @Test
    void 다른모임에대한투표가있을때_모임에대한모든시간후보지_전체조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme);
        Meet compareMeet = createAndSaveMeet(2L, theme);

        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);

        TimeSlot compareTimeSlot1 = createAndSaveTimeSlot(compareMeet, LocalDateTime.now(), false);
        TimeSlot compareTimeSlot2 = createAndSaveTimeSlot(compareMeet, LocalDateTime.now(), false);


        // when
        System.out.println("=====Logic Start=====");

        List<TimeSlot> allByMeetId = timeSlotJpaRepository.findAllByMeetId(saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(allByMeetId.size()).isEqualTo(2);

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
}