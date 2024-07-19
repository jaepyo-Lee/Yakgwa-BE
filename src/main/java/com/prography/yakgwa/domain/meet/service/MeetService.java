package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.meet.impl.MeetWriter;
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
import com.prography.yakgwa.global.format.exception.param.DataIntegrateException;
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
    private final MeetReader meetReader;
    private final ParticipantReader participantReader;
    private final MeetStatusJudger meetStatusJudger;
    private final PlaceSlotReader placeSlotReader;
    private final TimeSlotReader timeSlotReader;

    /**
     * Todo
     * 요청 dto에서 투표확정시간과 투표가능시간을 둘다 받는데 이것을 모임 생성할때
     * 해당 값들을 검증하는게 맞을까? 둘다 null이거나 둘다 값이 들어가있는경우 예외처리해야하는데 현재는 meetWriter.write에서 처리하고 있음
     */
    @Transactional
    public Meet create(MeetCreateRequestDto requestDto) {
        User user = userReader.read(requestDto.getCreatorId());
        Meet meet = meetWriter.write(requestDto.toMeetWriteDto(), requestDto.toConfirmPlaceDto(), requestDto.toConfirmTimeDto());
        participantWriter.registLeader(meet, user);
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
            List<PlaceSlot> placeSlots = placeSlotReader.readAllConfirmByMeetId(meet.getId());
            List<TimeSlot> timeSlots = timeSlotReader.readAllConfirmByMeetId(meet.getId());
            TimeSlot timeSlot = null;
            PlaceSlot placeSlot = null;
            if(placeSlots.size()>1){
                throw new DataIntegrateException();
            }else if (placeSlots.size()==1){
                placeSlot = placeSlots.stream().findFirst().get();
            }
            if(timeSlots.size()>1){
                throw new DataIntegrateException();
            }else if(timeSlots.size()==1){
                timeSlot = timeSlots.stream().findFirst().get();
            }
            list.add(MeetWithVoteAndStatus.builder()
                    .meet(meet)
                    .timeSlot(timeSlot)
                    .placeSlot(placeSlot)
                    .meetStatus(meetStatus).build());
        }

        return list;
    }
}
