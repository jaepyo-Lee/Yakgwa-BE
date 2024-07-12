package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.impl.ParticipantReader;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.impl.*;
import com.prography.yakgwa.domain.vote.service.req.EnableTimeRequestDto;
import com.prography.yakgwa.domain.vote.service.req.PlaceInfosByMeetStatus;
import com.prography.yakgwa.domain.vote.service.req.TimeInfosByMeetStatus;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmVoteException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyTimeConfirmVoteException;
import com.prography.yakgwa.global.format.exception.vote.ParticipantConfirmException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class VoteService {
    private final MeetReader meetReader;
    private final PlaceVoteReader placeVoteReader;
    private final TimeVoteReader timeVoteReader;
    private final PlaceSlotReader placeSlotReader;
    private final TimeSlotReader timeSlotReader;
    private final PlaceVoteWriter placeVoteWriter;
    private final UserReader userReader;
    private final TimeVoteWriter timeVoteWriter;
    private final TimeSlotWriter timeSlotWriter;
    private final ParticipantReader participantReader;


    public PlaceInfosByMeetStatus findPlaceInfoWithMeetStatus(Long userId, Long meetId) {
        Meet meet = meetReader.read(meetId); //일단 모임 존재여부 확인용
        if (placeSlotReader.existConfirm(meetId)) { //장소확정되었을때
            PlaceSlot placeSlot = placeSlotReader.readConfirmOrNullByMeetId(meetId);
            return PlaceInfosByMeetStatus.builder()
                    .meetStatus(MeetStatus.CONFIRM)
                    .places(List.of(placeSlot.getPlace()))
                    .build();
        } else {
            List<PlaceVote> placeVoteOfUserInMeet = placeVoteReader.findAllPlaceVoteOfUserInMeet(userId, meetId);
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
                        .places(null)
                        .build();
            }
        }
    }

    public TimeInfosByMeetStatus findTimeInfoWithMeetStatus(Long userId, Long meetId) {
        Meet meet = meetReader.read(meetId); //일단 모임 존재여부 확인용

        if (timeSlotReader.existConfirm(meetId)) { // 시간확정되었을때
            TimeSlot timeSlot = timeSlotReader.readConfirmOrNullByMeetId(meetId);
            return TimeInfosByMeetStatus.builder()
                    .meetStatus(MeetStatus.CONFIRM)
                    .timeSlots(List.of(timeSlot))
                    .build();
        } else {
            List<TimeVote> timeVoteOfUserInMeet = timeVoteReader.findAllTimeVoteOfUserInMeet(userId, meet.getId());
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

    /**
     * 1. 확정여부 확인
     * 2. 기존투표 삭제
     * 3. 새로운 투표생성
     */
    public List<PlaceVote> votePlace(Long userId, Long meetId, List<Long> placeSlotIds) {
        if (placeSlotReader.existConfirm(meetId)) {
            throw new AlreadyPlaceConfirmVoteException();
        }
        User user = userReader.read(userId);
        placeVoteWriter.deleteAllVoteOfUser(user, meetId);

        List<PlaceSlot> choosePlaceSlot = placeSlotReader.findAllByIds(placeSlotIds);
        return placeVoteWriter.writeAll(user, choosePlaceSlot);
    }

    /**
     * 1. 투표확정여부 확인
     * 2. 기존의 투표목록 삭제
     * 3. 시간 투표항목에 없는 시간이 들어왔는지 확인 -> 새로운 항목이라면 추가
     * 4. 시간 투표 생성
     */
    public List<TimeVote> voteTime(Long userId, Long meetId, EnableTimeRequestDto requestDto) {
        if (timeSlotReader.existConfirm(meetId)) {
            throw new AlreadyTimeConfirmVoteException();
        }
        Meet meet = meetReader.read(meetId);
        List<TimeSlot> allTimeSlotsInMeet = timeSlotReader.readByMeetId(meetId);
        User user = userReader.read(userId);
        timeVoteWriter.deleteAllVoteOfUser(user, meetId);

        List<LocalDateTime> notExistTime = requestDto.getEnableTimes().stream()
                .filter(time -> allTimeSlotsInMeet.stream()
                        .noneMatch(timeSlot -> timeSlot.getTime().isEqual(time)))
                .toList();

        timeSlotWriter.writeAll(meet, notExistTime);

        List<TimeSlot> chooseTimeSlot = timeSlotReader.findAllByMeetIdAndTimes(meetId, requestDto.getEnableTimes());

        return timeVoteWriter.writeAll(user, chooseTimeSlot);
    }

    public void confirmPlace(Long userId, Long meetId, Long confirmPlaceSlotId) {
        Participant participant = participantReader.readByUserIdAndMeetId(userId, meetId);
        if (!participant.isLeader()) {
            throw new ParticipantConfirmException();
        }
        PlaceSlot placeSlot = placeSlotReader.read(confirmPlaceSlotId);
        placeSlot.confirm();
    }

    public void confirmTime(Long userId, Long meetId, Long confirmTimeSlotId) {
        Participant participant = participantReader.readByUserIdAndMeetId(userId, meetId);
        if (!participant.isLeader()) {
            throw new ParticipantConfirmException();
        }
        TimeSlot timeSlot = timeSlotReader.read(confirmTimeSlotId);
        timeSlot.confirm();
    }
}