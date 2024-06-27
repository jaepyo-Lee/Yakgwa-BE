package com.prography.yakgwa.domain.place.impl;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class PlaceReader {
    private final PlaceJpaRepository placeJpaRepository;
    public Optional<Place> readByMapxAndMapy(String mapx, String mapy){
        return placeJpaRepository.findByMapxAndMapy(mapx, mapy);
    }
}
