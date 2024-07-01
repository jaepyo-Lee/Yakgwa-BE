package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class PlaceSlotWriter {
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    public PlaceSlot write(final PlaceSlot placeSlot) {
        return placeSlotJpaRepository.save(placeSlot);
    }
}
