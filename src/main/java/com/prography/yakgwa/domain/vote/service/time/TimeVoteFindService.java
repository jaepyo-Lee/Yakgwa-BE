package com.prography.yakgwa.domain.vote.service.time;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.ConfirmChecker;
import com.prography.yakgwa.domain.meet.impl.TimeConfirmChecker;
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
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus.*;

@Transactional(readOnly = true)
@Service
@Slf4j
public class TimeVoteFindService implements VoteFinder<TimeInfosByMeetStatus> {
    private final ParticipantJpaRepository participantJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final TimeVoteJpaRepository timeVoteJpaRepository;
    private final VoteCounter voteCounter;
    private final MeetJpaRepository meetJpaRepository;
    private final ConfirmChecker confirmChecker;

    public TimeVoteFindService(ParticipantJpaRepository participantJpaRepository,
                               TimeSlotJpaRepository timeSlotJpaRepository,
                               TimeVoteJpaRepository timeVoteJpaRepository,
                               VoteCounter voteCounter,
                               MeetJpaRepository meetJpaRepository,
                               TimeConfirmChecker confirmChecker) {
        this.participantJpaRepository = participantJpaRepository;
        this.timeSlotJpaRepository = timeSlotJpaRepository;
        this.timeVoteJpaRepository = timeVoteJpaRepository;
        this.voteCounter = voteCounter;
        this.meetJpaRepository = meetJpaRepository;
        this.confirmChecker = confirmChecker;
    }

    @Override
    public TimeInfosByMeetStatus findVoteInfoWithStatusOf(Long userId, Long meetId) {
        Meet meet = findMeetById(meetId);
        Participant participant = participantJpaRepository.findByUserIdAndMeetId(userId, meetId)
                .orElseThrow(NotFoundParticipantException::new);
        boolean isConfirm = confirmChecker.isConfirm(meet); /*verifyConfirmAndConfirmTimePossible(meet)*/

        if (isConfirm) { // 시간확정되었을때
            List<TimeSlot> timeSlot = timeSlotJpaRepository.findAllConfirmByMeetId(meetId);
            return TimeInfosByMeetStatus.of(CONFIRM, timeSlot, meet);
        } else {
            if (meet.isVoteTimeEnd()) { //시간은 지났지만 확정은 안됌 BEFROE_CONFIRM
                List<TimeVote> allInMeet = timeVoteJpaRepository.findAllByMeetId(meet.getId());
                List<TimeSlot> collect = voteCounter.findMaxVoteTimeSlotFrom(meet);
                return TimeInfosByMeetStatus.of(BEFORE_CONFIRM, collect, meet);
            } else {
                List<TimeVote> timeVoteOfUserInMeet = timeVoteJpaRepository.findAllByTimeSlotOfUser(userId, meet.getId());
                if (isUserVoteTimeSlot(timeVoteOfUserInMeet)) { //사용자가 투표했을때
                    return TimeInfosByMeetStatus.of(VoteStatus.VOTE,
                            timeVoteOfUserInMeet.stream()
                            .map(TimeVote::getTimeSlot)
                            .toList(), meet);
                } else { //사용자가 투표 안했을때
                    return TimeInfosByMeetStatus.of(BEFORE_VOTE, List.of(), meet);
                }
            }
        }
    }


    private static boolean isUserVoteTimeSlot(List<TimeVote> timeVoteOfUserInMeet) {
        return !timeVoteOfUserInMeet.isEmpty();
    }

    private Meet findMeetById(Long meetId) {
        return meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
    }
}
