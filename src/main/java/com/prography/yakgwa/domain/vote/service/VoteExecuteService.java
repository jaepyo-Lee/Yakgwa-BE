package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.req.EnableTimeRequestDto;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyTimeConfirmVoteException;
import com.prography.yakgwa.global.format.exception.vote.NotValidVotePlaceException;
import com.prography.yakgwa.global.format.exception.vote.NotValidVoteTimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Transactional
@RequiredArgsConstructor
@Service
public class VoteExecuteService {
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final TimeVoteJpaRepository timeVoteJpaRepository;
    private final MeetJpaRepository meetJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;

    /**
     * 1. 투표확정여부 확인
     * 2. 기존의 투표목록 삭제
     * 3. 시간 투표항목에 없는 시간이 들어왔는지 확인 -> 새로운 항목이라면 추가
     * 4. 시간 투표 생성
     */
    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:21
     * Finish-Date) 2024-07-30
     */
    public List<TimeVote> voteTime(Long userId, Long meetId, EnableTimeRequestDto requestDto) {
        if (!participantJpaRepository.existsByUserIdAndMeetId(userId, meetId)) {
            throw new NotFoundParticipantException();
        }
        if (isTimeConfirmedFrom(meetId)) {
            throw new AlreadyTimeConfirmVoteException();
        }
        Meet meet = findBy(meetId);
        List<TimeSlot> allTimeSlotsInMeet = timeSlotJpaRepository.findAllByMeetId(meetId);
        if (isValidTimeVote(allTimeSlotsInMeet, meet)) {
            throw new AlreadyTimeConfirmVoteException();
        }
        for (LocalDateTime enableTime : requestDto.getEnableTimes()) {
            if (isWithinVotePeriodFrom(meet, enableTime)) {
                throw new NotValidVoteTimeException();
            }
        }
        User user = userJpaRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        timeVoteJpaRepository.deleteAllByUserIdAndTimeSlotIdIn(user, meetId);

        List<LocalDateTime> notExistTime = findNotExistTimeInTimeSlot(requestDto, allTimeSlotsInMeet);

        List<TimeSlot> newTimeSlots = notExistTime.stream()
                .map(time -> TimeSlot.builder()
                        .meet(meet)
                        .time(time)
                        .confirm(false).build())
                .toList();

        timeSlotJpaRepository.saveAll(newTimeSlots);

        List<TimeSlot> chooseTimeSlot = timeSlotJpaRepository.findAllByMeetIdAndTimes(meetId, requestDto.getEnableTimes().stream().toList());

        List<TimeVote> timeVotes = chooseTimeSlot.stream().map(timeSlot -> TimeVote.builder()
                .timeSlot(timeSlot)
                .user(user)
                .build()).toList();
        List<TimeVote> timeVotes1 = timeVoteJpaRepository.saveAll(timeVotes);
        return timeVotes1;
    }

    /**
     * 1. 확정여부 확인
     * 2. 기존투표 삭제
     * 3. 새로운 투표생성
     */
    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:21
     * Finish-Date)
     */
    public List<PlaceVote> votePlace(Long userId, Long meetId, Set<Long> placeSlotIds) {
        if (placeSlotJpaRepository.isConfirmFrom(meetId)) {
            throw new AlreadyPlaceConfirmException();
        }
        if (!participantJpaRepository.existsByUserIdAndMeetId(userId, meetId)) {
            throw new NotFoundParticipantException();
        }
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meetId);
        boolean isContainSlot = placeSlots.stream().allMatch(placeSlot -> placeSlotIds.contains(placeSlot.getId()));
        if (!isContainSlot) {
            throw new NotValidVotePlaceException();
        }
        User user = userJpaRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        placeVoteJpaRepository.deleteAllByUserIdAndMeetId(user, meetId);
        List<PlaceSlot> choosePlaceSlot = placeSlotJpaRepository.findAllById(placeSlotIds);
        List<PlaceVote> placeVotes = choosePlaceSlot.stream()
                .map(placeSlot -> PlaceVote.builder()
                        .placeSlot(placeSlot)
                        .user(user)
                        .build())
                .toList();
        return placeVoteJpaRepository.saveAll(placeVotes);
    }

    private Meet findBy(Long meetId) {
        return meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
    }

    private boolean isTimeConfirmedFrom(Long meetId) {
        return timeSlotJpaRepository.existsConfirmFrom(meetId);
    }

    private List<LocalDateTime> findNotExistTimeInTimeSlot(EnableTimeRequestDto requestDto, List<TimeSlot> allTimeSlotsInMeet) {
        return requestDto.getEnableTimes().stream()
                .filter(time -> allTimeSlotsInMeet.stream()
                        .noneMatch(timeSlot -> timeSlot.getTime().isEqual(time)))
                .toList();
    }

    private boolean isValidTimeVote(List<TimeSlot> allTimeSlotsInMeet, Meet meet) {
        return isAlreadyConfirm(allTimeSlotsInMeet) ||
                isOverVotePeriodFrom(meet);
    }

    private boolean isAlreadyConfirm(List<TimeSlot> allTimeSlotsInMeet) {
        return allTimeSlotsInMeet.stream().anyMatch(TimeSlot::getConfirm);
    }

    private static boolean isWithinVotePeriodFrom(Meet meet, LocalDateTime enableTime) {
        return meet.getPeriod().getEndDate().isBefore(enableTime.toLocalDate()) || meet.getPeriod().getStartDate().isAfter(enableTime.toLocalDate());
    }

    private static boolean isOverVotePeriodFrom(Meet meet) {
        return meet.getCreatedDate().plusHours(meet.getValidInviteHour()).isBefore(LocalDateTime.now());
    }
}
