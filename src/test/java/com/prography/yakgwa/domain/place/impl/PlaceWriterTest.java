package com.prography.yakgwa.domain.place.impl;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.place.entity.Place;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class PlaceWriterTest extends IntegrationTestSupport {
    @AfterEach
    void init() {
        deleter.deleteAll();
    }
    @Test
    void 존재하지않는장소일때저장() {
        // given
        String title = "title";
        String mapx = "mapx";
        String mapy = "mapy";
        Place place = Place.builder().title(title).mapx(mapx).mapy(mapy).build();

        // when
        System.out.println("=====Logic Start=====");
        placeWriter.writeIfNotExist(place);
        System.out.println("=====Logic End=====");
        // then
        List<Place> all = placeJpaRepository.findAll();
        assertAll(() -> assertThat(all.size()).isOne());
    }

    @Test
    void 존재할때저장이아닌조회_저장안한다는뜻() {
        // given
        String title = "title";
        String mapx = "mapx";
        String mapy = "mapy";
        Place place = Place.builder().title(title).mapx(mapx).mapy(mapy).build();
        placeJpaRepository.save(place);
        // when
        System.out.println("=====Logic Start=====");

        placeWriter.writeIfNotExist(place);

        System.out.println("=====Logic End=====");
        // then
        List<Place> all = placeJpaRepository.findAll();
        assertAll(() -> assertThat(all.size()).isOne());
    }
}