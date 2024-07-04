package com.prography.yakgwa.domain.vote.controller.res;

import com.prography.yakgwa.domain.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NewPlaceSlotResponse {
    private PlaceSlotInMeet placeInfoDto;

    @Getter
    @Builder
    private static class PlaceSlotInMeet {
        private String title;
        private String address;
        private String mapx;
        private String mapy;
    }

    public static NewPlaceSlotResponse of(Place place) {
        return NewPlaceSlotResponse.builder()
                .placeInfoDto(PlaceSlotInMeet.builder()
                        .address(place.getAddress())
                        .title(place.getTitle())
                        .mapx(place.getMapx())
                        .mapy(place.getMapy())
                        .build())
                .build();
    }
}
