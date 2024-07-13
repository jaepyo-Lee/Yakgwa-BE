package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.participant.impl.ParticipantReader;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
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
import java.util.Map;
import java.util.stream.Collectors;

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
    private final MeetStatusJudger meetStatusJudger;

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-13
     * Finish-Date)
     * 원래 BEFORE_VOTE이면 PLACE가 NULL인데 빈리스트 반환으로 변경
     */
    public PlaceInfosByMeetStatus findPlaceInfoWithMeetStatus(Long userId, Long meetId) {
        Meet meet = meetReader.read(meetId);
        Participant participant = participantReader.readByUserIdAndMeetId(userId, meetId);

        boolean isConfirm = meetStatusJudger.verifyConfirmAndConfirmPlacePossible(meet);

        if (isConfirm) { //장소확정되었을때
            PlaceSlot placeSlot = placeSlotReader.readConfirmOrNullByMeetId(meetId);
            return PlaceInfosByMeetStatus.builder()
                    .voteStatus(VoteStatus.CONFIRM)
                    .places(List.of(placeSlot.getPlace()))
                    .build();
        } else {
            if (meet.getCreatedDate().plusHours(meet.getValidInviteHour()).isBefore(LocalDateTime.now())) { //시간은 지났지만 확정은 안됌 BEFROE_CONFIRM
                if (participant.getMeetRole().equals(MeetRole.LEADER)) {
                    List<PlaceVote> allInMeet = placeVoteReader.findAllInMeet(meet.getId());
                    // 맵으로 후보지 세기
                    Map<PlaceSlot, Long> placeSlotVoteCounts = allInMeet.stream()
                            .collect(Collectors.groupingBy(PlaceVote::getPlaceSlot, Collectors.counting()));

                    // 투표 최대값
                    long maxVoteCount = placeSlotVoteCounts.values().stream()
                            .max(Long::compare)
                            .orElse(0L);

                    // 최대값을 가진것으로 확인
                    List<Place> maxVotePlaces = placeSlotVoteCounts.entrySet().stream()
                            .filter(entry -> entry.getValue() == maxVoteCount)
                            .map(placeSlotLongEntry -> placeSlotLongEntry.getKey().getPlace())
                            .collect(Collectors.toList());

                    return PlaceInfosByMeetStatus.builder()
                            .voteStatus(VoteStatus.BEFORE_CONFIRM)
                            .places(maxVotePlaces)
                            .build();
                }
            }
            List<PlaceVote> placeVoteOfUserInMeet = placeVoteReader.findAllPlaceVoteOfUserInMeet(userId, meetId);
            if (!placeVoteOfUserInMeet.isEmpty()) { //사용자가 투표했을때
                return PlaceInfosByMeetStatus.builder()
                        .voteStatus(VoteStatus.VOTE)
                        .places(placeVoteOfUserInMeet.stream()
                                .map(placeVote -> placeVote.getPlaceSlot().getPlace())
                                .toList())
                        .build();
            } else { //사용자가 투표 안했을때
                return PlaceInfosByMeetStatus.builder()
                        .voteStatus(VoteStatus.BEFORE_VOTE)
                        .places(placeVoteOfUserInMeet.stream()
                                .map(placeVote -> placeVote.getPlaceSlot().getPlace())
                                .toList())
                        .build();
            }
        }
    }

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-13
     * Finish-Date)
     */
    public TimeInfosByMeetStatus findTimeInfoWithMeetStatus(Long userId, Long meetId) {
        Meet meet = meetReader.read(meetId); //일단 모임 존재여부 확인용
        Participant participant = participantReader.readByUserIdAndMeetId(userId, meetId);
        boolean isConfirm = meetStatusJudger.verifyConfirmAndConfirmTimePossible(meet);

        if (isConfirm) { // 시간확정되었을때
            TimeSlot timeSlot = timeSlotReader.readConfirmOrNullByMeetId(meetId);
            return TimeInfosByMeetStatus.builder()
                    .voteStatus(VoteStatus.CONFIRM)
                    .timeSlots(List.of(timeSlot))
                    .build();
        } else {
            if (meet.getCreatedDate().plusHours(meet.getValidInviteHour()).isBefore(LocalDateTime.now())) { //시간은 지났지만 확정은 안됌 BEFROE_CONFIRM
                if (participant.getMeetRole().equals(MeetRole.LEADER)) {
                    List<TimeVote> allInMeet = timeVoteReader.readAllInMeet(meet.getId());
                    // 맵으로 후보지 세기
                    Map<TimeSlot, Long> timeSlotVoteCounts = allInMeet.stream()
                            .collect(Collectors.groupingBy(TimeVote::getTimeSlot, Collectors.counting()));

                    // 투표 최대값
                    long maxVoteCount = timeSlotVoteCounts.values().stream()
                            .max(Long::compare)
                            .orElse(0L);

                    // 최대값을 가진것으로 확인
                    List<TimeSlot> collect = timeSlotVoteCounts.entrySet().stream()
                            .filter(entry -> entry.getValue() == maxVoteCount)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());

                    return TimeInfosByMeetStatus.builder()
                            .voteStatus(VoteStatus.BEFORE_CONFIRM)
                            .timeSlots(collect)
                            .build();
                }
            }
            List<TimeVote> timeVoteOfUserInMeet = timeVoteReader.findAllTimeVoteOfUserInMeet(userId, meet.getId());
            if (!timeVoteOfUserInMeet.isEmpty()) { //사용자가 투표했을때
                return TimeInfosByMeetStatus.builder()
                        .voteStatus(VoteStatus.VOTE)
                        .timeSlots(timeVoteOfUserInMeet.stream()
                                .map(TimeVote::getTimeSlot)
                                .toList())
                        .build();
            } else { //사용자가 투표 안했을때
                return TimeInfosByMeetStatus.builder()
                        .voteStatus(VoteStatus.BEFORE_VOTE)
                        .timeSlots(timeVoteOfUserInMeet.stream()
                                .map(TimeVote::getTimeSlot)
                                .toList())
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