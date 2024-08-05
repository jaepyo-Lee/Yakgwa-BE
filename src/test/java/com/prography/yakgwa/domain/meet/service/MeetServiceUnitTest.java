package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.meet.impl.MeetWriter;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.impl.ParticipantWriter;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MeetServiceUnitTest {
    @Mock
    MeetWriter meetWriter;
    @Mock
    ParticipantWriter participantWriter;
    @Mock
    MeetStatusJudger meetStatusJudger;
    @InjectMocks
    @Spy
    MeetService meetService;
    @Mock
    UserJpaRepository userJpaRepository;
    @Mock
    ParticipantJpaRepository participantJpaRepository;

}