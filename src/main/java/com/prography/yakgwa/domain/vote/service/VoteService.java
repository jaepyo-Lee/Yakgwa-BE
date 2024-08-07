package com.prography.yakgwa.domain.vote.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.alarm.AlarmProcessor;
import com.prography.yakgwa.domain.common.schedule.AlarmScheduler;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.req.PlaceInfosByMeetStatus;
import com.prography.yakgwa.domain.vote.service.req.TimeInfosByMeetStatus;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.param.DataIntegrateException;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import com.prography.yakgwa.global.format.exception.slot.NotFoundPlaceSlotException;
import com.prography.yakgwa.global.format.exception.slot.NotFoundTimeSlotException;
import com.prography.yakgwa.global.format.exception.slot.NotMatchSlotInMeetException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyTimeConfirmVoteException;
import com.prography.yakgwa.global.format.exception.vote.NotValidConfirmTimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus.BEFORE_CONFIRM;
import static com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus.VOTE;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class VoteService {
    private final MeetStatusJudger meetStatusJudger;
    private final MeetJpaRepository meetJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final TimeVoteJpaRepository timeVoteJpaRepository;
    private final VoteCounter voteCounter;
    private final AlarmScheduler alarmScheduler;

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-13
     * Finish-Date)
     * 원래 BEFORE_VOTE이면 PLACE가 NULL인데 빈리스트 반환으로 변경
     */
    public PlaceInfosByMeetStatus findPlaceInfoWithMeetStatus(Long userId, Long meetId) {
        Meet meet = findBy(meetId);
        Participant participant = participantJpaRepository.findByUserIdAndMeetId(userId, meetId)
                .orElseThrow(NotFoundParticipantException::new);

        boolean isConfirm = meetStatusJudger.verifyConfirmAndConfirmPlacePossible(meet);

        if (isConfirm) { //장소확정되었을때
            List<PlaceSlot> placeSlots = placeSlotJpaRepository.findConfirmByMeetId(meetId);
            if (isCorrectConfirmPlaceSlotSize(placeSlots)) {
                log.info("{}번 모임의 장소투표 데이터확인", meetId);
                throw new DataIntegrateException();
            }
            return PlaceInfosByMeetStatus.of(VoteStatus.CONFIRM, placeSlots, meet);
        } else {
            if (isOverVotePeriodFrom(meet)) { //시간은 지났지만 확정은 안됌 BEFROE_CONFIRM
                List<PlaceVote> allInMeet = placeVoteJpaRepository.findAllInMeet(meet.getId());
                List<PlaceSlot> placeSlotList = voteCounter.findMaxVotePlaceSlotFrom(meet);
                return PlaceInfosByMeetStatus.of(BEFORE_CONFIRM, placeSlotList, meet);
            }
            List<PlaceVote> placeVoteOfUserInMeet = placeVoteJpaRepository.findAllByUserIdAndMeetId(userId, meetId);
            if (isUserVotePlace(placeVoteOfUserInMeet)) { //사용자가 투표했을때
                return PlaceInfosByMeetStatus.of(VOTE, placeVoteOfUserInMeet.stream()
                        .map(PlaceVote::getPlaceSlot)
                        .toList(), meet);
            } else { //사용자가 투표 안했을때
                return PlaceInfosByMeetStatus.of(VoteStatus.BEFORE_VOTE, List.of(), meet);
            }
        }
    }

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-13
     * Finish-Date)
     */
    public TimeInfosByMeetStatus findTimeInfoWithMeetStatus(Long userId, Long meetId) {
        Meet meet = findBy(meetId);
        Participant participant = participantJpaRepository.findByUserIdAndMeetId(userId, meetId)
                .orElseThrow(NotFoundParticipantException::new);
        boolean isConfirm = meetStatusJudger.verifyConfirmAndConfirmTimePossible(meet);

        if (isConfirm) { // 시간확정되었을때
            List<TimeSlot> timeSlot = timeSlotJpaRepository.findAllConfirmByMeetId(meetId);
            if (isCorrectConfirmTimeSlotSize(timeSlot)) {
                log.info("{}번 모임의 시간투표 데이터확인", meetId);
                throw new DataIntegrateException();
            }
            return TimeInfosByMeetStatus.builder()
                    .voteStatus(VoteStatus.CONFIRM)
                    .timeSlots(timeSlot)
                    .meet(meet)
                    .build();
        } else {
            if (isOverVotePeriodFrom(meet)) { //시간은 지났지만 확정은 안됌 BEFROE_CONFIRM
                List<TimeVote> allInMeet = timeVoteJpaRepository.findAllByMeetId(meet.getId());
                List<TimeSlot> collect = voteCounter.findMaxVoteTimeSlotFrom(meet);

                return TimeInfosByMeetStatus.builder()
                        .voteStatus(BEFORE_CONFIRM)
                        .timeSlots(collect)
                        .meet(meet)
                        .build();
            } else {
                List<TimeVote> timeVoteOfUserInMeet = timeVoteJpaRepository.findAllByTimeSlotOfUser(userId, meet.getId());
                if (isUserVoteTimeSlot(timeVoteOfUserInMeet)) { //사용자가 투표했을때
                    return TimeInfosByMeetStatus.builder()
                            .voteStatus(VoteStatus.VOTE)
                            .timeSlots(timeVoteOfUserInMeet.stream()
                                    .map(TimeVote::getTimeSlot)
                                    .toList())
                            .meet(meet)
                            .build();
                } else { //사용자가 투표 안했을때
                    return TimeInfosByMeetStatus.builder()
                            .voteStatus(VoteStatus.BEFORE_VOTE)
                            .timeSlots(List.of())
                            .meet(meet)
                            .build();
                }
            }
        }
    }

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-25
     * Finish-Date) 2024-07-30
     */
    /**
     * Todo
     * Work) 확정가능시간에대한 테스트코드
     * Write-Date)
     * Finish-Date)
     */
    public void confirmPlace(Long userId, Long meetId, Long confirmPlaceSlotId) throws JsonProcessingException {
        Meet meet = meetJpaRepository.findById(meetId).orElseThrow(NotFoundMeetException::new);
        if (placeSlotJpaRepository.isConfirmFrom(meetId)) {
            throw new AlreadyPlaceConfirmException();
        }
        if(meet.getValidConfirmTime().isBefore(LocalDateTime.now())){
            throw new NotValidConfirmTimeException();
        }
        Participant participant = participantJpaRepository.findByUserIdAndMeetId(userId, meetId)
                .orElseThrow(NotFoundParticipantException::new);
        PlaceSlot placeSlot = placeSlotJpaRepository.findById(confirmPlaceSlotId)
                .orElseThrow(NotFoundPlaceSlotException::new);
        if (!isPlaceSlotMatchMeet(meetId, placeSlot)) {
            throw new NotMatchSlotInMeetException();
        }
        placeSlot.confirm();
        if(meetStatusJudger.isConfirm(meet)){
            alarmScheduler.registerAlarm(meet);
        }
    }

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-25
     * Finish-Date) 2024-07-30
     */
    /**
     * Todo
     * Work) 확정가능시간에대한 테스트코드
     * Write-Date)
     * Finish-Date)
     */
    public void confirmTime(Long meetId,Long userId, Long confirmTimeSlotId) throws JsonProcessingException {
        Meet meet = meetJpaRepository.findById(meetId).orElseThrow(NotFoundMeetException::new);
        if (isTimeConfirmedFrom(meetId)) {
            throw new AlreadyTimeConfirmVoteException();
        }
        if(meet.getValidConfirmTime().isBefore(LocalDateTime.now())){
            throw new NotValidConfirmTimeException();
        }
        Participant participant = participantJpaRepository.findByUserIdAndMeetId(userId, meetId)
                .orElseThrow(NotFoundParticipantException::new);
        TimeSlot timeSlot = timeSlotJpaRepository.findById(confirmTimeSlotId)
                .orElseThrow(NotFoundTimeSlotException::new);
        timeSlot.confirm();
        if(meetStatusJudger.isConfirm(meet)){
            alarmScheduler.registerAlarm(meet);
        }
    }

    private boolean isPlaceSlotMatchMeet(Long meetId, PlaceSlot placeSlot) {
        return placeSlot.getMeet().getId().equals(meetId);
    }


    private static boolean isUserVoteTimeSlot(List<TimeVote> timeVoteOfUserInMeet) {
        return !timeVoteOfUserInMeet.isEmpty();
    }

    private static boolean isCorrectConfirmTimeSlotSize(List<TimeSlot> timeSlot) {
        return timeSlot.size() > 1;
    }

    private Meet findBy(Long meetId) {
        return meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
    }

    private boolean isTimeConfirmedFrom(Long meetId) {
        return timeSlotJpaRepository.existsConfirmFrom(meetId);
    }

    private static boolean isUserVotePlace(List<PlaceVote> placeVoteOfUserInMeet) {
        return !placeVoteOfUserInMeet.isEmpty();
    }

    private static boolean isOverVotePeriodFrom(Meet meet) {
        return meet.getCreatedDate().plusHours(meet.getValidInviteHour()).isBefore(LocalDateTime.now());
    }

    private static boolean isCorrectConfirmPlaceSlotSize(List<PlaceSlot> placeSlots) {
        return placeSlots.size() > 1;
    }

}