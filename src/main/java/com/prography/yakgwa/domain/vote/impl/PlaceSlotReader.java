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

    public boolean existConfirm(Long meetId) {
        return repository.existsByMeetId(meetId);
    }

    public List<PlaceSlot> findAllByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public PlaceSlot read(Long confirmPlaceSlotId) {
        return repository.findById(confirmPlaceSlotId)
                .orElseThrow(() -> new RuntimeException("없는 모임장소 후보지입니다."));
    }
}
