package com.prography.yakgwa.domain.participant.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class ParticipantWriter {
    private final ParticipantJpaRepository participantJpaRepository;

    public Participant registLeader(Meet meet, User user) {
        Participant participant = Participant.builder()
                .meetRole(MeetRole.LEADER)
                .meet(meet)
                .user(user)
                .build();
        return participantJpaRepository.save(participant);
    }
}
