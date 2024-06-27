package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetWriter;
import com.prography.yakgwa.domain.meet.impl.dto.MeetWriteDto;
import com.prography.yakgwa.domain.meet.service.req.MeetCreateRequestDto;
import com.prography.yakgwa.domain.participant.impl.ParticipantWriter;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import com.prography.yakgwa.domain.vote.impl.PlaceVoteWriter;
import com.prography.yakgwa.domain.vote.impl.TimeVoteWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetService {
    private final MeetWriter meetWriter;
    private final UserReader userReader;
    private final ParticipantWriter participantWriter;
    private final PlaceVoteWriter placeVoteWriter;
    private final TimeVoteWriter timeVoteWriter;

    @Transactional
    public Meet create(MeetCreateRequestDto requestDto) {
        User user = userReader.read(requestDto.getCreatorId());
        Meet meet = meetWriter.write(MeetWriteDto.of(requestDto));
        participantWriter.registLeader(meet, user);
        placeVoteWriter.confirmAndWrite(user, meet, requestDto.getPlaceInfo());
        timeVoteWriter.confirmAndWrite(user, meet, requestDto.getMeetTime());
        return meet;
    }
}
