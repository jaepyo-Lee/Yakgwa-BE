package com.prography.yakgwa.domain.participant.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.impl.ParticipantWriter;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantWriter participantWriter;
    private final UserReader userReader;
    private final MeetReader meetReader;
    public Participant enterMeet(Long userId,Long meetId){
        User user = userReader.read(userId);
        Meet meet = meetReader.read(meetId);
        return participantWriter.registParticipant(meet, user);
    }
}
