package com.prography.yakgwa.domain.place.impl;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class PlaceReaderTest {
    @Autowired
    PlaceJpaRepository placeJpaRepository;
    @Autowired
    PlaceReader placeReader;

    @AfterEach
    void init() {
        placeJpaRepository.deleteAll();
    }

    @Test
    void 위치좌표를통해Impl_장소조회Test() {
        // given
        String mapx = "321";
        String mapy = "123";

        Place place1 = Place.builder()
                .mapy(mapy).mapx(mapx).link("link").title("title").roadAddress("roadAddress").address("address").description("description").category("category").telephone("telephone")
                .build();

        Place place2 = Place.builder()
                .mapy("321").mapx("123").link("link").title("title").roadAddress("roadAddress").address("address").description("description").category("category").telephone("telephone")
                .build();

        placeJpaRepository.save(place1);
        placeJpaRepository.save(place2);

        // when
        System.out.println("=====Logic Start=====");

        Optional<Place> place = placeReader.readByMapxAndMapy(mapx, mapy);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(place).isPresent(),
                () -> assertThat(place.get().getMapx()).isEqualTo(mapx),
                () -> assertThat(place.get().getMapy()).isEqualTo(mapy));
    }
}