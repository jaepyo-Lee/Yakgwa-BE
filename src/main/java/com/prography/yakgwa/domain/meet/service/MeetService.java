package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetManager;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.domain.meet.impl.MeetWriter;
import com.prography.yakgwa.domain.meet.impl.dto.MeetWriteDto;
import com.prography.yakgwa.domain.meet.service.req.MeetCreateRequestDto;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.meet.service.res.MeetInfoWithParticipant;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.impl.ParticipantReader;
import com.prography.yakgwa.domain.participant.impl.ParticipantWriter;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import com.prography.yakgwa.domain.vote.impl.VoteManager;
import com.prography.yakgwa.domain.vote.impl.PlaceVoteWriter;
import com.prography.yakgwa.domain.vote.impl.TimeVoteWriter;
import com.prography.yakgwa.domain.vote.impl.dto.VoteInfoWithStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetService {
    private final MeetWriter meetWriter;
    private final UserReader userReader;
    private final ParticipantWriter participantWriter;
    private final PlaceVoteWriter placeVoteWriter;
    private final TimeVoteWriter timeVoteWriter;
    private final MeetReader meetReader;
    private final ParticipantReader participantReader;
    private final MeetManager meetManager;

    @Transactional
    public Meet create(MeetCreateRequestDto requestDto) {
        User user = userReader.read(requestDto.getCreatorId());
        Meet meet = meetWriter.write(MeetWriteDto.of(requestDto));
        participantWriter.registLeader(meet, user);
        placeVoteWriter.confirmAndWrite(user, meet, requestDto.getPlaceInfo());
        timeVoteWriter.confirmAndWrite(user, meet, requestDto.getMeetTime());
        return meet;
    }

    public MeetInfoWithParticipant findWithParticipant(Long meetId) {
        Meet meet = meetReader.read(meetId);
        List<Participant> participants = participantReader.readAllByMeetId(meetId);
        return MeetInfoWithParticipant.of(meet, participants);
    }

    public List<MeetWithVoteAndStatus> findWithStatus(Long userId) {
        List<Participant> participants = participantReader.readAllByUserId(userId);
        return participants.stream()
                .map(participant -> meetManager.createMeetWithVoteAndStatus(participant, userId))
                .toList();
    }

}
