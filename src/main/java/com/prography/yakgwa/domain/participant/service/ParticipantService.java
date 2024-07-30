package com.prography.yakgwa.domain.participant.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.impl.ParticipantWriter;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantWriter participantWriter;
    private final MeetJpaRepository meetJpaRepository;
    private final UserJpaRepository userJpaRepository;

    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:19
     * Finish-Date) 2024-07-29
     */
    public Participant enterMeet(Long userId,Long meetId){
        User user = userJpaRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        Meet meet = meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
        return participantWriter.registParticipant(meet, user);
    }
}
