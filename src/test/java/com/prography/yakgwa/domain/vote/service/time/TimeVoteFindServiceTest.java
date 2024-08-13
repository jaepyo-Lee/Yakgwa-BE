package com.prography.yakgwa.domain.vote.service.time;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.service.time.res.TimeInfosByMeetStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
class TimeVoteFindServiceTest extends IntegrationTestSupport {

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 약과원_투표안했는데모임시간확정되었을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusDays(1L);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, time, true);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, time2, false);

        // when
        System.out.println("=====Logic Start=====");

        TimeInfosByMeetStatus timeInfoWithMeetStatus = timeVoteFinder.findVoteInfoWithStatusOf(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(timeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.CONFIRM),
                () -> assertThat(timeInfoWithMeetStatus.getTimeSlots().size()).isEqualTo(1));
    }

    @Test
    void 약과원_투표했는데모임시간확정되었을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusDays(1L);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, time, true);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, time2, false);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot, saveUser);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot2, saveUser);

        // when
        System.out.println("=====Logic Start=====");

        TimeInfosByMeetStatus timeInfoWithMeetStatus = timeVoteFinder.findVoteInfoWithStatusOf(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(timeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.CONFIRM),
                () -> assertThat(timeInfoWithMeetStatus.getTimeSlots().size()).isEqualTo(1));
    }

    @Test
    void 약과원_모임시간이지나지않았을때_확정안되었을때_투표안했을떄_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusDays(1L);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, time, false);

        // when
        System.out.println("=====Logic Start=====");

        TimeInfosByMeetStatus timeInfoWithMeetStatus = timeVoteFinder.findVoteInfoWithStatusOf(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(timeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_VOTE),
                () -> assertThat(timeInfoWithMeetStatus.getTimeSlots().size()).isEqualTo(0));
    }

    @Test
    void 약과원_모임시간이지나지않았을때_확정안되었을때_투표했을떄_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, theme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusDays(1L);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, time, false);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet, time.plusDays(1L), false);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot, saveUser);
        dummyCreater.createAndSaveTimeVote(saveTimeSlot2, saveUser);

        // when
        System.out.println("=====Logic Start=====");

        TimeInfosByMeetStatus timeInfoWithMeetStatus = timeVoteFinder.findVoteInfoWithStatusOf(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");

        // then
        assertAll(() -> assertThat(timeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.VOTE),
                () -> assertThat(timeInfoWithMeetStatus.getTimeSlots().size()).isEqualTo(2));
    }

}