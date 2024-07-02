package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ImplService
@RequiredArgsConstructor
public class TimeSlotReader {
    private final TimeSlotJpaRepository repository;
    public List<TimeSlot> readByMeetId(Long meetId){
        return repository.findByMeetId(meetId);
    }

    public TimeSlot readConfirmOrNullByMeetId(Long meetId) {
        return repository.findConfirmByMeetId(meetId)
                .orElse(null);
    }
}
