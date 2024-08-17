package com.prography.yakgwa.domain.vote.entity.place;

import com.prography.yakgwa.domain.place.entity.Place;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlaceSlotTest {
    @Test
    void 확정시키기() {
        // given
        PlaceSlot placeSlot = PlaceSlot.builder().confirm(false).build();

        // when
        System.out.println("=====Logic Start=====");

        placeSlot.confirm();

        System.out.println("=====Logic End=====");
        // then
        assertThat(placeSlot.getConfirm()).isTrue();
    }

    @Test
    void 확정여부확인() {
        // given
        PlaceSlot placeSlot1 = PlaceSlot.builder().confirm(false).build();
        PlaceSlot placeSlot2 = PlaceSlot.builder().confirm(true).build();
        // when
        System.out.println("=====Logic Start=====");
        boolean confirm1 = placeSlot1.isConfirm();
        boolean confirm2 = placeSlot2.isConfirm();
        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(confirm1).isFalse(), () -> assertThat(confirm2).isTrue());}

    @Test
    void 같은장소인지확인() {
        // given
        String mapy = "mapy";
        String mapx = "mapx";
        String title = "title";
        Place place = Place.builder().mapy(mapy).mapx(mapx).title(title).build();
        PlaceSlot placeSlot =PlaceSlot.builder().place(place).build();

        // when
        System.out.println("=====Logic Start=====");
        boolean samePlace = placeSlot.isSamePlace(title, mapx, mapy);
        System.out.println("=====Logic End=====");

        // then
        assertThat(samePlace).isTrue();
    }

    @Test
    void id값이같으면같은객체() {
        // given
        PlaceSlot placeSlot = PlaceSlot.builder().id(1L).build();
        PlaceSlot compare = PlaceSlot.builder().id(1L).build();

        // when
        System.out.println("=====Logic Start=====");
        System.out.println("=====Logic End=====");
        // then
        assertThat(placeSlot.equals(compare)).isTrue();
    }
    @Test
    void 같은참조객체라면같은객체() {
        // given
        PlaceSlot placeSlot = PlaceSlot.builder().id(1L).build();

        // when
        System.out.println("=====Logic Start=====");
        System.out.println("=====Logic End=====");
        // then
        assertThat(placeSlot.equals(placeSlot)).isTrue();
    }
}