package com.prography.yakgwa.domain.place.impl;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@ImplService
@RequiredArgsConstructor
public class PlaceWriter {
    private final PlaceJpaRepository placeJpaRepository;

    @Async
    public void writeNotExist(Place place) {
        placeJpaRepository.findByTitleAndMapxAndMapy(place.getTitle(), place.getMapx(), place.getMapy())
                .orElseGet(() -> placeJpaRepository.save(place));
    }
}
