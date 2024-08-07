package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.VoteCounter;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Boolean.TRUE;

@ImplService
@RequiredArgsConstructor
public class MeetStatusJudger {
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final TimeVoteJpaRepository timeVoteJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final VoteCounter voteCounter;

    /**
     * Work) 테스트 코드
     * Write-Date) 2024-07-13
     * Finish-Date) 2024-07-13
     */
    public MeetStatus judge(Meet meet, User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validInviteTime = meet.getCreatedDate().plusHours(meet.getValidInviteHour());

        if (validInviteTime.isBefore(now) && !isConfirm(meet)) {
            return handleExpiredVoteTime(meet);
        } else if (isConfirm(meet)) {
            return MeetStatus.CONFIRM;
        } else {
            return handleBeforeVote(meet, user);
        }
    }

    private MeetStatus handleExpiredVoteTime(Meet meet) {

        boolean isConfirmPlace = verifyConfirmAndConfirmPlacePossible(meet);

        boolean isConfirmTime = verifyConfirmAndConfirmTimePossible(meet);

        if (!isConfirmTime || !isConfirmPlace) {
            return MeetStatus.BEFORE_CONFIRM;
        }
        return MeetStatus.CONFIRM;
    }

    /**
     * Todo
     * Work) 테스트 코드, judge메서드에서 함께 테스트되어 후순위
     * Write-Date) 2024-07-13
     * Finish-Date)
     */
    // 최다득표 후보지가 확정 되어있는지 확인하는 메서드
    public boolean verifyConfirmAndConfirmPlacePossible(Meet meet) {
        List<PlaceSlot> placeSlots = voteCounter.findMaxVotePlaceSlotFrom(meet);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validInviteTime = meet.getCreatedDate().plusHours(meet.getValidInviteHour());
        if (validInviteTime.isBefore(now) && placeSlots.size() <= 1) {
            placeSlots.forEach(PlaceSlot::confirm);
        }
        return isPlaceConfirm(meet);
    }

    /**
     * Todo
     * Work) 테스트 코드, judge메서드에서 함께 테스트되어 후순위
     * Write-Date) 2024-07-13
     * Finish-Date)
     */
    // 최다 득표가 있어서 확정짓는 또는 확정을 못짓는
    public boolean verifyConfirmAndConfirmTimePossible(Meet meet) {
        List<TimeSlot> timeSlots = voteCounter.findMaxVoteTimeSlotFrom(meet);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validInviteTime = meet.getCreatedDate().plusHours(meet.getValidInviteHour());
        if (validInviteTime.isBefore(now) && timeSlots.size() <= 1) {
            timeSlots.forEach(TimeSlot::confirm);
        }
        return isTimeConfirm(meet);
    }


    private boolean isPlaceConfirm(Meet meet) {
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meet.getId());
        return placeSlots.stream().anyMatch(placeSlot -> placeSlot.getConfirm().equals(TRUE));
    }


    private MeetStatus handleBeforeVote(Meet meet, User user) {
        boolean isVotePlace = placeVoteJpaRepository.existsByUserIdAndMeetId(user.getId(), meet.getId());
        boolean isVoteTime = timeVoteJpaRepository.existsByUserIdInMeet(user.getId(), meet.getId());

        if (!isVotePlace && !isVoteTime) {
            return MeetStatus.BEFORE_VOTE;
        }

        return MeetStatus.VOTE;
    }


    public boolean isConfirm(Meet meet) {
        return isTimeConfirm(meet) && isPlaceConfirm(meet);
    }


    private boolean isTimeConfirm(Meet meet) {
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllByMeetId(meet.getId());
        return timeSlots.stream().anyMatch(timeSlot -> timeSlot.getConfirm().equals(TRUE));
    }
}
