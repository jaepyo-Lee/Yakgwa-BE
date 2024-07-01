package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class TimeSlotWriter {
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    public TimeSlot write(final TimeSlot timeSlot) {
        return timeSlotJpaRepository.save(timeSlot);
    }
}
