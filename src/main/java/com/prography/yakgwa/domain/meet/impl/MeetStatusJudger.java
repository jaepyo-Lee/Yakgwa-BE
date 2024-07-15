package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.impl.PlaceSlotReader;
import com.prography.yakgwa.domain.vote.impl.PlaceVoteReader;
import com.prography.yakgwa.domain.vote.impl.TimeSlotReader;
import com.prography.yakgwa.domain.vote.impl.TimeVoteReader;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@ImplService
@RequiredArgsConstructor
public class MeetStatusJudger {
    private final PlaceSlotReader placeSlotReader;
    private final TimeSlotReader timeSlotReader;
    private final PlaceVoteReader placeVoteReader;
    private final TimeVoteReader timeVoteReader;

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
     * Work) 테스트 코드
     * Write-Date) 2024-07-13
     * Finish-Date)
     */
    // 최다 득표가 있어서 확정짓는 또는 확정을 못짓는
    public boolean verifyConfirmAndConfirmPlacePossible(Meet meet) {
        List<PlaceVote> allInMeet = placeVoteReader.findAllInMeet(meet.getId());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validInviteTime = meet.getCreatedDate().plusHours(meet.getValidInviteHour());

        Map<PlaceSlot, Long> placeSlotVoteCounts = allInMeet.stream()
                .collect(Collectors.groupingBy(PlaceVote::getPlaceSlot, Collectors.counting()));

        long maxVoteCount = placeSlotVoteCounts.values().stream()
                .max(Long::compare)
                .orElse(0L);

        List<PlaceSlot> placeSlots = placeSlotVoteCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVoteCount)
                .map(Map.Entry::getKey)
                .toList();
        if (validInviteTime.isBefore(now) && placeSlots.size() <= 1) {
            placeSlots.forEach(PlaceSlot::confirm);
        }
        return isPlaceConfirm(meet);
    }


    /**
     * Todo
     * Work) 테스트 코드
     * Write-Date) 2024-07-13
     * Finish-Date)
     */
    // 최다 득표가 있어서 확정짓는 또는 확정을 못짓는
    public boolean verifyConfirmAndConfirmTimePossible(Meet meet) {
        List<TimeVote> timeVotes = timeVoteReader.readAllInMeet(meet.getId());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validInviteTime = meet.getCreatedDate().plusHours(meet.getValidInviteHour());
        Map<TimeSlot, Long> timeSlotVoteCounts = timeVotes.stream()
                .collect(Collectors.groupingBy(TimeVote::getTimeSlot, Collectors.counting()));

        long maxVoteCount = timeSlotVoteCounts.values().stream()
                .max(Long::compare)
                .orElse(0L);

        List<TimeSlot> timeSlots = timeSlotVoteCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVoteCount)
                .map(Map.Entry::getKey)
                .toList();
        if (validInviteTime.isBefore(now) && timeSlots.size() <= 1) {
            timeSlots.forEach(TimeSlot::confirm);
        }
        return isTimeConfirm(meet);
    }

    private MeetStatus handleBeforeVote(Meet meet, User user) {
        boolean isVotePlace = placeVoteReader.existsByUserIdAndMeetId(user.getId(), meet.getId());
        boolean isVoteTime = timeVoteReader.existsByUserIdInMeet(user.getId(), meet.getId());

        if (!isVotePlace && !isVoteTime) {
            return MeetStatus.BEFORE_VOTE;
        }

        return MeetStatus.VOTE;
    }


    private boolean isConfirm(Meet meet) {
        return isTimeConfirm(meet) && isPlaceConfirm(meet);
    }

    private boolean isPlaceConfirm(Meet meet) {
        List<PlaceSlot> placeSlots = placeSlotReader.readAllByMeetId(meet.getId());
        return placeSlots.stream().anyMatch(placeSlot -> placeSlot.getConfirm().equals(TRUE));
    }

    private boolean isTimeConfirm(Meet meet) {
        List<TimeSlot> timeSlots = timeSlotReader.readAllByMeetId(meet.getId());
        return timeSlots.stream().anyMatch(timeSlot -> timeSlot.getConfirm().equals(TRUE));
    }
}
