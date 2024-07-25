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

    public Optional<Place> readByMapxAndMapy(String mapx, String mapy) {
        return placeJpaRepository.findByMapxAndMapy(mapx, mapy);
    }
    public Optional<Place> readByMapxAndMapyAndTitle(String mapx, String mapy,String title) {
        return placeJpaRepository.findByTitleAndMapxAndMapy(title,mapx, mapy);
    }

    public Place read(Long placeId) {
        return placeJpaRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("장소를 찾을수 없습니다."));
    }
}
