package com.prography.yakgwa.domain.place.repository;

import com.prography.yakgwa.domain.place.entity.Place;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlaceJpaRepositoryTest {
    @Autowired
    PlaceJpaRepository placeJpaRepository;

    @AfterEach
    void init() {
        placeJpaRepository.deleteAll();
    }

    @Test
    void 좌표를이용해_장소조회Test() {
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

        Optional<Place> place = placeJpaRepository.findByMapxAndMapy(mapx, mapy);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(place).isPresent(),
                () -> assertThat(place.get().getMapx()).isEqualTo(mapx),
                () -> assertThat(place.get().getMapy()).isEqualTo(mapy));
    }
}