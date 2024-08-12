package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
@ImplService
public class PlaceConfirmChecker implements ConfirmChecker{
    private final PlaceSlotJpaRepository placeSlotJpaRepository;

    @Override
    public boolean isConfirm(Meet meet) {
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meet.getId());
        return placeSlots.stream().anyMatch(placeSlot -> placeSlot.getConfirm().equals(TRUE));
    }
}
