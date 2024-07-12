package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ImplService
@RequiredArgsConstructor
public class TimeSlotReader {
    private final TimeSlotJpaRepository repository;

    public List<TimeSlot> readByMeetId(Long meetId) {
        return repository.findByMeetId(meetId);
    }

    /**
     * Work) 테스트코드 작성
     * Write-Date) 2024-07-12
     * Finish-Date) 2024-07-12
     */
    public TimeSlot readConfirmOrNullByMeetId(Long meetId) {
        return repository.findConfirmByMeetId(meetId)
                .orElse(null);
    }

    public boolean existConfirm(Long meetId) {
        return repository.existsByMeetId(meetId);
    }

    public List<TimeSlot> findAllByMeetIdAndTimes(Long meetId, List<LocalDateTime> times) {
        return repository.findAllByMeetIdAndTimes(meetId, times);
    }

    public TimeSlot read(Long confirmTimeSlotId) {
        return repository.findById(confirmTimeSlotId)
                .orElseThrow(() -> new RuntimeException("등록되지 않은 시간 후보입니다."));
    }
}
