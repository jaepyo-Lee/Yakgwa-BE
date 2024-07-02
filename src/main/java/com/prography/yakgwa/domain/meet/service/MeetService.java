package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
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
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private final MeetStatusJudger meetStatusJudger;
    private final PlaceSlotReader placeSlotReader;
    private final TimeSlotReader timeSlotReader;

    @Transactional
    public Meet create(MeetCreateRequestDto requestDto) {
        User user = userReader.read(requestDto.getCreatorId());
        Meet meet = meetWriter.write(MeetWriteDto.of(requestDto));
        participantWriter.registLeader(meet, user);
        placeVoteWriter.confirmAndWrite(requestDto.getPlaceInfo());
        timeVoteWriter.confirmAndWrite(user, meet, requestDto.getMeetTime());
        return meet;
    }

    public MeetInfoWithParticipant findWithParticipant(Long meetId) {
        Meet meet = meetReader.read(meetId);
        List<Participant> participants = participantReader.readAllByMeetId(meetId);
        return MeetInfoWithParticipant.of(meet, participants);
    }

    public List<MeetWithVoteAndStatus> findWithStatus(Long userId) {
        User user = userReader.read(userId);
        List<Participant> participants = participantReader.readAllByUserId(userId);

        List<MeetWithVoteAndStatus> list = new ArrayList<>();
        for (Participant participant : participants) {
            Meet meet = participant.getMeet();
            MeetStatus meetStatus = meetStatusJudger.judge(meet, user);
            PlaceSlot placeSlot=placeSlotReader.readConfirmOrNullByMeetId(meet.getId());;
            TimeSlot timeSlot = timeSlotReader.readConfirmOrNullByMeetId(meet.getId());
            list.add(MeetWithVoteAndStatus.builder()
                    .meet(meet)
                    .timeSlot(timeSlot)
                    .placeSlot(placeSlot)
                    .meetStatus(meetStatus).build());
        }

        return list;
    }
}
