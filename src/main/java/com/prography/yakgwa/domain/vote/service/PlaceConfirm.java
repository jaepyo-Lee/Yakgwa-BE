package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.service.impl.VoteCounter;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ImplService
@RequiredArgsConstructor
public class PlaceConfirm implements VoteConfirm {
    private final VoteCounter voteCounter;

    /**
     * Todo
     * Work) 테스트 코드
     * Write-Date) 2024-07-13
     * Finish-Date)
     */
    @Override
    public boolean confirmMaxOf(Meet meet) {
        List<PlaceSlot> placeSlots = voteCounter.findMaxVotePlaceSlotFrom(meet);
        // 해당 메서드를 호출하는곳은 스케줄러밖에 없는데, 조건때문에 before걸어놓으면 확정 못시킬수있음
        // 최댓값이 존재할때만 확정짓는다.
        if (placeSlots.size() == 1) {
            placeSlots.forEach(PlaceSlot::confirm);
            return true;
        }
        return false;
    }
}
