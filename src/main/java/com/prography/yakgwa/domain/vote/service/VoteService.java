package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.impl.PlaceSlotReader;
import com.prography.yakgwa.domain.vote.impl.PlaceVoteReader;
import com.prography.yakgwa.domain.vote.impl.TimeSlotReader;
import com.prography.yakgwa.domain.vote.impl.TimeVoteReader;
import com.prography.yakgwa.domain.vote.service.req.PlaceInfosByMeetStatus;
import com.prography.yakgwa.domain.vote.service.req.TimeInfosByMeetStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final MeetReader meetReader;
    private final PlaceVoteReader placeVoteReader;
    private final TimeVoteReader timeVoteReader;
    private final PlaceSlotReader placeSlotReader;
    private final TimeSlotReader timeSlotReader;


    public PlaceInfosByMeetStatus findPlaceInfoWithMeetStatus(Long userId, Long meetId) {
        Meet meet = meetReader.read(meetId); //일단 모임 존재여부 확인용
        if (placeSlotReader.existConfirm(meetId)) { //장소확정되었을때
            PlaceSlot placeSlot = placeSlotReader.readConfirmOrNullByMeetId(meetId);
            return PlaceInfosByMeetStatus.builder()
                    .meetStatus(MeetStatus.CONFIRM)
                    .places(List.of(placeSlot.getPlace()))
                    .build();
        } else {
            List<PlaceSlot> placeSlots = placeSlotReader.readByMeetId(meetId);
            List<PlaceVote> placeVoteOfUserInMeet = placeVoteReader.findAllPlaceVoteOfUserInMeet(userId, placeSlots.stream().map(PlaceSlot::getId).toList());
            if (!placeVoteOfUserInMeet.isEmpty()) { //사용자가 투표했을때
                return PlaceInfosByMeetStatus.builder()
                        .meetStatus(MeetStatus.VOTE)
                        .places(placeVoteOfUserInMeet.stream()
                                .map(placeVote -> placeVote.getPlaceSlot().getPlace())
                                .toList())
                        .build();
            } else { //사용자가 투표 안했을때
                return PlaceInfosByMeetStatus.builder()
                        .meetStatus(MeetStatus.BEFORE_VOTE)
                        .places(placeVoteOfUserInMeet.stream()
                                .map(placeVote -> placeVote.getPlaceSlot().getPlace())
                                .toList())
                        .build();
            }
        }
    }

    public TimeInfosByMeetStatus findTimeInfoWithMeetStatus(Long userId, Long meetId) {
        Meet meet = meetReader.read(meetId); //일단 모임 존재여부 확인용

        if (timeVoteReader.existsConfirm(meetId)) { // 시간확정되었을때
            TimeSlot timeSlot = timeSlotReader.readConfirmOrNullByMeetId(meetId);
            return TimeInfosByMeetStatus.builder()
                    .meetStatus(MeetStatus.CONFIRM)
                    .timeSlots(List.of(timeSlot))
                    .build();
        } else {
            List<TimeSlot> timeSlots = timeSlotReader.readByMeetId(meetId);
            List<TimeVote> timeVoteOfUserInMeet = timeVoteReader.findAllTimeVoteOfUserInMeet(userId, timeSlots.stream().map(TimeSlot::getId).toList());

            if (!timeVoteOfUserInMeet.isEmpty()) { //사용자가 투표했을때
                return TimeInfosByMeetStatus.builder()
                        .meetStatus(MeetStatus.VOTE)
                        .timeSlots(timeVoteOfUserInMeet.stream().map(TimeVote::getTimeSlot).toList())
                        .build();
            } else { //사용자가 투표 안했을때
                return TimeInfosByMeetStatus.builder()
                        .meetStatus(MeetStatus.BEFORE_VOTE)
                        .timeSlots(timeVoteOfUserInMeet.stream().map(TimeVote::getTimeSlot).toList())
                        .build();
            }
        }
    }
}
