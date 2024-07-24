package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.meet.impl.MeetWriter;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.participant.impl.ParticipantReader;
import com.prography.yakgwa.domain.participant.impl.ParticipantWriter;
import com.prography.yakgwa.domain.user.impl.UserReader;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.impl.PlaceSlotReader;
import com.prography.yakgwa.domain.vote.impl.TimeSlotReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MeetServiceUnitTest {
    @Mock
    UserReader userReader;
    @Mock
    MeetWriter meetWriter;
    @Mock
    ParticipantWriter participantWriter;
    @Mock
    MeetReader meetReader;
    @Mock
    ParticipantReader participantReader;
    @Mock
    MeetStatusJudger meetStatusJudger;
    @Mock
    PlaceSlotReader placeSlotReader;
    @Mock
    TimeSlotReader timeSlotReader;
    @InjectMocks
    @Spy
    MeetService meetService;

    @Test
    void 나의모임확인_확정상태이고_확정시간에서1시간이지난모임들_조회() {
        // given
        MeetWithVoteAndStatus build1 = MeetWithVoteAndStatus.builder()
                .meetStatus(MeetStatus.CONFIRM)
                .timeSlot(TimeSlot.builder().time(LocalDateTime.now().plusHours(2)).build())
                .build();
        MeetWithVoteAndStatus build2 = MeetWithVoteAndStatus.builder()
                .meetStatus(MeetStatus.CONFIRM)
                .timeSlot(TimeSlot.builder().time(LocalDateTime.now().plusHours(3)).build())
                .build();
        MeetWithVoteAndStatus build3 = MeetWithVoteAndStatus.builder()
                .meetStatus(MeetStatus.CONFIRM)
                .timeSlot(TimeSlot.builder().time(LocalDateTime.now().plusHours(3)).build())
                .build();
        MeetWithVoteAndStatus build6 = MeetWithVoteAndStatus.builder()
                .meetStatus(MeetStatus.CONFIRM)
                .timeSlot(TimeSlot.builder().time(LocalDateTime.now().minusHours(3)).build())
                .build();
        MeetWithVoteAndStatus build4 = MeetWithVoteAndStatus.builder()
                .meetStatus(MeetStatus.BEFORE_VOTE)
                .build();
        MeetWithVoteAndStatus build5 = MeetWithVoteAndStatus.builder()
                .meetStatus(MeetStatus.BEFORE_CONFIRM)
                .timeSlot(TimeSlot.builder().time(LocalDateTime.now().plusHours(3)).build())
                .build();
        List<MeetWithVoteAndStatus> list = List.of(build1, build2, build3, build4, build5, build6);
        when(meetService.findWithStatus(anyLong())).thenReturn(list);
        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> postConfirm = meetService.findPostConfirm(1L);

        System.out.println("=====Logic End=====");
        // then
        assertThat(postConfirm.size()).isEqualTo(3);
    }
}