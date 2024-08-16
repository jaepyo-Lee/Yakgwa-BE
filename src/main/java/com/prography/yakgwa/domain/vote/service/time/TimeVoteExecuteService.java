package com.prography.yakgwa.domain.vote.service.time;

import com.prography.yakgwa.domain.common.schedule.TaskScheduleManager;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetConfirmChecker;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.VoteExecuter;
import com.prography.yakgwa.domain.vote.service.time.req.EnableTimeRequestDto;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import com.prography.yakgwa.global.format.exception.slot.NotFoundTimeSlotException;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyTimeConfirmVoteException;
import com.prography.yakgwa.global.format.exception.vote.NotValidConfirmTimeException;
import com.prography.yakgwa.global.format.exception.vote.NotValidMeetVoteDateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class TimeVoteExecuteService implements VoteExecuter<TimeVote, EnableTimeRequestDto> {
    private final MeetJpaRepository meetJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final TaskScheduleManager alarmScheduler;
    private final UserJpaRepository userJpaRepository;
    private final TimeVoteJpaRepository timeVoteJpaRepository;
    private final MeetConfirmChecker confirmChecker;

    /**
     * Todo
     * Work) 동시성체크해야할듯
     * Write-Date)
     * Finish-Date)
     */
    @Override
    public List<TimeVote> vote(Long userId, Long meetId, EnableTimeRequestDto requestDto) {
        if (isNotExistParticipant(userId, meetId)) {
            throw new NotFoundParticipantException();
        }
        if (isTimeConfirmedFrom(meetId)) {
            throw new AlreadyTimeConfirmVoteException();
        }
        Meet meet = findMeetById(meetId);
        List<TimeSlot> allTimeSlotsInMeet = timeSlotJpaRepository.findAllByMeetId(meetId);
        if (isValidTimeVote(allTimeSlotsInMeet, meet)) {
            throw new AlreadyTimeConfirmVoteException();
        }
        for (LocalDateTime enableTime : requestDto.getEnableTimes()) {
            if (isWithinVotePeriodFrom(meet, enableTime)) {
                throw new NotValidMeetVoteDateException();
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
        return timeVoteJpaRepository.saveAll(timeVotes);

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
    /**
     * Todo
     * Work) 동시성체크해야할듯
     * Write-Date)
     * Finish-Date)
     */
    @Override
    public void confirm(Long meetId, Long userId, Long timeSlotId) {
        Meet meet = findMeetById(meetId);
        if (isTimeConfirmedFrom(meetId)) {
            throw new AlreadyTimeConfirmVoteException();
        }
        if (meet.getConfirmTime().isBefore(LocalDateTime.now())) {
            throw new NotValidConfirmTimeException();
        }
        Participant participant = participantJpaRepository.findByUserIdAndMeetId(userId, meetId)
                .orElseThrow(NotFoundParticipantException::new);
        TimeSlot timeSlot = timeSlotJpaRepository.findById(timeSlotId)
                .orElseThrow(NotFoundTimeSlotException::new);
        timeSlot.confirm();
        if (confirmChecker.isMeetConfirm(meet)) {
            alarmScheduler.registerAlarm(meet, AlarmType.END_VOTE);
        }
    }


    private boolean isNotExistParticipant(Long userId, Long meetId) {
        return !participantJpaRepository.existsByUserIdAndMeetId(userId, meetId);
    }

    private Meet findMeetById(Long meetId) {
        return meetJpaRepository.findById(meetId).orElseThrow(NotFoundMeetException::new);
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
