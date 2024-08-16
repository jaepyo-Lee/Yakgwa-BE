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

    /**
     * Todo
     * Work) 비동기도 따로 테스트진행해야할거같은데, 해당 메서드 사용하는 부분에서는 동기로 진행되게해서 테스트하는 방법 있다. 추후 리팩토링하자
     * 사실 결과값, 로직보다는 비동기로 작동하는게 해당 메서드의 핵심
     * Write-Date)
     * Finish-Date)
     */
    @Async
    public void writeIfNotExist(Place place) {
        placeJpaRepository.findByTitleAndMapxAndMapy(place.getTitle(), place.getMapx(), place.getMapy())
                .orElseGet(() -> placeJpaRepository.save(place));
    }
}
