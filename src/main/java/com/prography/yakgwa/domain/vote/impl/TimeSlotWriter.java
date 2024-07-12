package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ImplService
@RequiredArgsConstructor
public class TimeSlotWriter {
    private final TimeSlotJpaRepository timeSlotJpaRepository;

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-12
     * Finish-Date) 2024-07-12
     * Memo) 다른로직 없어 생략
     */
    public TimeSlot write(final TimeSlot timeSlot) {
        return timeSlotJpaRepository.save(timeSlot);
    }

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-12
     * Finish-Date) 2024-07-12
     */
    public List<TimeSlot> writeAll(Meet meet, List<LocalDateTime> times) {
        List<TimeSlot> newTimeSlots = times.stream()
                .map(time -> TimeSlot.builder()
                        .meet(meet)
                        .time(time)
                        .confirm(false).build())
                .toList();
        return timeSlotJpaRepository.saveAll(newTimeSlots);
    }
}
