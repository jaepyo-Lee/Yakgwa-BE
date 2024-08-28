package com.prography.yakgwa.domain.vote.service.place;

import com.prography.yakgwa.domain.common.schedule.TaskScheduleManager;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetConfirmChecker;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.VoteExecuter;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import com.prography.yakgwa.global.format.exception.slot.NotFoundPlaceSlotException;
import com.prography.yakgwa.global.format.exception.slot.NotMatchSlotInMeetException;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmException;
import com.prography.yakgwa.global.format.exception.vote.NotValidConfirmTimeException;
import com.prography.yakgwa.global.format.exception.vote.NotValidVotePlaceException;
import com.prography.yakgwa.global.format.exception.vote.NotValidVoteTimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceVoteExecuteService implements VoteExecuter<PlaceVote, Set<Long>> {
    private final MeetJpaRepository meetJpaRepository;
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;
    private final TaskScheduleManager alarmScheduler;
    private final UserJpaRepository userJpaRepository;
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final MeetConfirmChecker confirmChecker;

    /**
     * Todo
     * Work) 동시성체크해야할듯
     * Write-Date)
     * Finish-Date)
     */
    @Override
    public List<PlaceVote> vote(Long userId, Long meetId, Set<Long> placeSlotIds) {
        Meet meet = meetJpaRepository.findById(meetId).orElseThrow(NotFoundMeetException::new);
        if (meet.isVoteTimeEnd()) {
            throw new NotValidVoteTimeException();
        }
        if (placeSlotJpaRepository.isConfirmFrom(meetId)) {
            throw new AlreadyPlaceConfirmException();
        }
        if (!participantJpaRepository.existsByUserIdAndMeetId(userId, meetId)) {
            throw new NotFoundParticipantException();
        }
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meetId);

        for(Long choosePlaceSlotId:placeSlotIds){
            List<Long> list = placeSlots.stream().map(PlaceSlot::getId).toList();
            if(!list.contains(choosePlaceSlotId)){
                throw new NotValidVotePlaceException();
            }
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
    public void confirm(Long meetId, Long userId, Long placeSlotId) {
        Meet meet = meetJpaRepository.findById(meetId).orElseThrow(NotFoundMeetException::new);
        if (placeSlotJpaRepository.isConfirmFrom(meetId)) {
            throw new AlreadyPlaceConfirmException();
        }
        if (meet.isConfirmTimeEnd()) {
            log.info("meetId:{},userId:{},placeSlotId:{} 확정가능시간지남", meetId, userId, placeSlotId);
            throw new NotValidConfirmTimeException();
        }
        Participant participant = participantJpaRepository.findByUserIdAndMeetId(userId, meetId)
                .orElseThrow(NotFoundParticipantException::new);
        PlaceSlot placeSlot = placeSlotJpaRepository.findById(placeSlotId)
                .orElseThrow(NotFoundPlaceSlotException::new);
        if (!isPlaceSlotMatchMeet(meetId, placeSlot)) {
            throw new NotMatchSlotInMeetException();
        }
        placeSlot.confirm();
        if (confirmChecker.isMeetConfirm(meet)) {
            alarmScheduler.registerAlarm(meet, AlarmType.END_VOTE);
        }
    }

    private boolean isPlaceSlotMatchMeet(Long meetId, PlaceSlot placeSlot) {
        return placeSlot.isEqualsMeet(meetId);
    }
}
