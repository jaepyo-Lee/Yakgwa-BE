package com.prography.yakgwa.domain.place.impl;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class PlaceWriterTest {
    @Autowired
    PlaceWriter placeWriter;
    @Autowired
    PlaceJpaRepository placeJpaRepository;

    @AfterEach
    void init() {
        placeJpaRepository.deleteAll();
    }

    @Test
    void Impl장소_생성Test() {
        // given
        String mapx = "321";
        String mapy = "123";

        Place place = Place.builder()
                .mapy(mapy).mapx(mapx).link("link").title("title").roadAddress("roadAddress").address("address").description("description").category("category").telephone("telephone")
                .build();

        // when
        System.out.println("=====Logic Start=====");

        Place savePlace = placeWriter.write(place);

        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(savePlace.getMapx()).isEqualTo(place.getMapx()),
                ()-> assertThat(savePlace.getMapy()).isEqualTo(place.getMapy()));
    }

}