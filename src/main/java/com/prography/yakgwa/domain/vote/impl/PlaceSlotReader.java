package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.global.format.exception.slot.NotFoundPlaceSlotException;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@ImplService
@RequiredArgsConstructor
public class PlaceSlotReader {
    private final PlaceSlotJpaRepository repository;

    public List<PlaceSlot> readAllByMeetId(Long meetId) {
        return repository.findAllByMeetId(meetId);
    }

    public List<PlaceSlot> readAllConfirmByMeetId(Long meetId) {
        return repository.findConfirmByMeetId(meetId);
    }

    public boolean existConfirm(Long meetId) {
        return repository.existsByMeetId(meetId);
    }

    public List<PlaceSlot> findAllByIds(Set<Long> ids) {
        return repository.findAllById(ids);
    }

    public PlaceSlot read(Long confirmPlaceSlotId) {
        return repository.findById(confirmPlaceSlotId)
                .orElseThrow(NotFoundPlaceSlotException::new);
    }
}
