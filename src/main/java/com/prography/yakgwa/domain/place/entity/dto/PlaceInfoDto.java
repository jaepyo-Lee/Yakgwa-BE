package com.prography.yakgwa.domain.place.entity.dto;

import com.prography.yakgwa.domain.place.entity.Place;
import lombok.Getter;

@Getter
public class PlaceInfoDto {
    private String title;
    private String link;
    private String category;
    private String description;
    private String telephone;
    private String address;
    private String roadAddress;
    private String mapx;
    private String mapy;

    public Place toEntity(){
        return Place.builder()
                .title(title)
                .link(link)
                .category(category)
                .description(description)
                .telephone(telephone)
                .address(address)
                .roadAddress(roadAddress)
                .mapx(mapx)
                .mapy(mapy)
                .build();
    }
}
