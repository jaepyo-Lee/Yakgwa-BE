package com.prography.yakgwa.domain.vote.service.place;

import com.prography.yakgwa.domain.common.schedule.AlarmScheduler;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
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
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.participant.NotFoundParticipantException;
import com.prography.yakgwa.global.format.exception.slot.NotFoundPlaceSlotException;
import com.prography.yakgwa.global.format.exception.slot.NotMatchSlotInMeetException;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmException;
import com.prography.yakgwa.global.format.exception.vote.NotValidConfirmTimeException;
import com.prography.yakgwa.global.format.exception.vote.NotValidVotePlaceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor
public class PlaceVoteExecuteService implements VoteExecuter<PlaceVote, Set<Long>> {
    private final MeetJpaRepository meetJpaRepository;
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;
    private final MeetStatusJudger meetStatusJudger;
    private final AlarmScheduler alarmScheduler;
    private final UserJpaRepository userJpaRepository;
    private final PlaceVoteJpaRepository placeVoteJpaRepository;


    @Override
    public List<PlaceVote> vote(Long userId, Long meetId, Set<Long> placeSlotIds) {
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
    @Override
    public void confirm(Long meetId, Long userId, Long placeSlotId) {
        Meet meet = meetJpaRepository.findById(meetId).orElseThrow(NotFoundMeetException::new);
        if (placeSlotJpaRepository.isConfirmFrom(meetId)) {
            throw new AlreadyPlaceConfirmException();
        }
        if (meet.getValidConfirmTime().isBefore(LocalDateTime.now())) {
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
        if (meetStatusJudger.isConfirm(meet)) {
            alarmScheduler.registerAlarm(meet);
        }
    }

    private boolean isPlaceSlotMatchMeet(Long meetId, PlaceSlot placeSlot) {
        return placeSlot.getMeet().getId().equals(meetId);
    }
}
