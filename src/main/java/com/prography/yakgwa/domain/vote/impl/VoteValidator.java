package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ImplService
@RequiredArgsConstructor
public class VoteValidator {
    private final PlaceSlotReader placeSlotReader;
    private final TimeSlotReader timeSlotReader;

    public boolean isPlaceVoteConfirm(Long meetId) {
        List<PlaceSlot> placeSlots = placeSlotReader.readByMeetId(meetId);
        return placeSlots.stream().anyMatch(PlaceSlot::isConfirm);
    }

    public boolean isTimeVoteConfirm(Long meetId) {
        List<TimeSlot> timeSlots = timeSlotReader.readByMeetId(meetId);
        return timeSlots.stream().anyMatch(TimeSlot::isConfirm);
    }
}
