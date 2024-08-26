package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
@ImplService
public class TimeConfirmChecker implements ConfirmChecker{

    private final TimeSlotJpaRepository timeSlotJpaRepository;

    @Override
    public boolean isConfirm(Meet meet) {
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllByMeetId(meet.getId());
        return timeSlots.stream().anyMatch(TimeSlot::isConfirm);
    }
}
