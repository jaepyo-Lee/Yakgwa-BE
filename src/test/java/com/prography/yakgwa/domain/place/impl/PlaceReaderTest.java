package com.prography.yakgwa.domain.place.impl;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
class PlaceReaderTest {
    @Autowired
    PlaceJpaRepository placeJpaRepository;
    @Autowired
    PlaceReader placeReader;

    @BeforeEach
    void init() {
        placeJpaRepository.deleteAll();
    }

    @Test
    void 위치좌표를통해Impl_장소조회Test() {
        // given
        Place place1 = savePlace(1L);
        Place place2 = savePlace(2L);

        // when
        System.out.println("=====Logic Start=====");

        Optional<Place> place = placeReader.readByMapxAndMapy("1", "1");

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(place).isPresent(),
                () -> assertThat(place.get().getMapx()).isEqualTo("1"),
                () -> assertThat(place.get().getMapy()).isEqualTo("1"));
    }

    @Test
    void 저장되어있는장소를id로_조회() {
        // given
        Place savePlace = savePlace(1L);

        // when
        System.out.println("=====Logic Start=====");

        Place read = placeReader.read(savePlace.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(read.getMapy()).isEqualTo(savePlace.getMapy()),
                () -> assertThat(read.getMapx()).isEqualTo(savePlace.getMapy()));

    }

    @Test
    void 저장안되어있는장소를id로_조회() {
        // given
        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThrows(RuntimeException.class, () -> placeReader.read(any()));

        System.out.println("=====Logic End=====");
    }

    private Place savePlace(Long id) {
        Place place = Place.builder()
                .mapy("" + id).mapx("" + id).link("link" + id).title("title" + id).roadAddress("roadAddress" + id).address("address" + id).description("description" + id).category("category" + id).telephone("telephone" + id)
                .build();
        return placeJpaRepository.save(place);
    }
}