package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ImplService
@RequiredArgsConstructor
public class PlaceSlotReader {
    private final PlaceSlotJpaRepository repository;

    public List<PlaceSlot> readByMeetId(Long meetId) {
        return repository.findByMeetId(meetId);
    }

    public PlaceSlot readConfirmOrNullByMeetId(Long meetId) {
        return repository.findConfirmByMeetId(meetId)
                .orElse(null);
    }

}
