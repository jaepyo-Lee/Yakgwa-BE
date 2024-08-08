package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.service.impl.VoteCounter;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ImplService
@RequiredArgsConstructor
public class TimeConfirm implements VoteConfirm {
    private final VoteCounter voteCounter;

    /**
     * Todo
     * Work) 테스트 코드
     * Write-Date) 2024-07-13
     * Finish-Date)
     */
    @Override
    public boolean confirmMaxOf(Meet meet) {
        List<TimeSlot> timeSlots = voteCounter.findMaxVoteTimeSlotFrom(meet);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validInviteTime = meet.getVoteTime();
        if (validInviteTime.isBefore(now) && timeSlots.size() <= 1) {
            timeSlots.forEach(TimeSlot::confirm);
            return true;
        }
        return false;
    }
}
