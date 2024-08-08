package com.prography.yakgwa.domain.vote.service.time;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.VoteFinder;
import com.prography.yakgwa.domain.vote.service.impl.VoteCounter;
import com.prography.yakgwa.domain.vote.service.time.res.TimeInfosByMeetStatus;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.param.DataIntegrateException;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus.BEFORE_CONFIRM;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Slf4j
public class TimeVoteFindService implements VoteFinder<TimeInfosByMeetStatus> {
    private final ParticipantJpaRepository participantJpaRepository;
    private final MeetStatusJudger meetStatusJudger;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final TimeVoteJpaRepository timeVoteJpaRepository;
    private final VoteCounter voteCounter;
    private final MeetJpaRepository meetJpaRepository;

    @Override
    public TimeInfosByMeetStatus findVoteInfoWithStatusOf(Long userId, Long meetId) {
        Meet meet = findMeetById(meetId);
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


    private static boolean isUserVoteTimeSlot(List<TimeVote> timeVoteOfUserInMeet) {
        return !timeVoteOfUserInMeet.isEmpty();
    }

    private static boolean isCorrectConfirmTimeSlotSize(List<TimeSlot> timeSlot) {
        return timeSlot.size() > 1;
    }

    private Meet findMeetById(Long meetId) {
        return meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
    }


    private static boolean isOverVotePeriodFrom(Meet meet) {
        return meet.getCreatedDate().plusHours(meet.getValidInviteHour()).isBefore(LocalDateTime.now());
    }
}
