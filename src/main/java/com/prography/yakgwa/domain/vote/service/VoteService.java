package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
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

    /**
     * 1. 확정여부 확인
     * 2. 기존투표 삭제
     * 3. 새로운 투표생성
    */
    public List<PlaceVote> votePlace(Long userId, Long meetId, List<Long> placeSlotIds) {
        if (placeSlotReader.existConfirm(meetId)) {
            throw new RuntimeException("이미 장소가 확정된 투표이기에 투표가 불가합니다.");
        }
        List<PlaceSlot> allPlaceSlotsInMeet = placeSlotReader.readByMeetId(meetId);
        User user = userReader.read(userId);
        placeVoteWriter.deleteAllVoteOfUser(user, allPlaceSlotsInMeet);

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
            throw new RuntimeException("이미 시간이 확정된 투표이기에 투표가 불가합니다.");
        }
        Meet meet = meetReader.read(meetId);
        List<TimeSlot> allTimeSlotsInMeet = timeSlotReader.readByMeetId(meetId);
        User user = userReader.read(userId);
        timeVoteWriter.deleteAllVoteOfUser(user, allTimeSlotsInMeet);

        List<LocalDateTime> notExistTime = requestDto.getEnableTimes().stream()
                .filter(time -> allTimeSlotsInMeet.stream()
                        .noneMatch(timeSlot -> timeSlot.getTime().isEqual(time)))
                .toList();

        timeSlotWriter.writeAll(meet, notExistTime);

        List<TimeSlot> chooseTimeSlot = timeSlotReader.findAllByMeetIdAndTimes(meetId, requestDto.getEnableTimes());

        return timeVoteWriter.writeAll(user, chooseTimeSlot);
    }
}