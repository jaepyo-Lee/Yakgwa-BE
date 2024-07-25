package com.prography.yakgwa.domain.place.entity;

import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PLACE_TABLE")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String link;
    private String category;
    private String description;
    private String telephone;
    private String address;
    private String roadAddress;
    private String mapx;
    private String mapy;

    public PlaceInfoDto toInfoDto(){
        return PlaceInfoDto.builder()
                .mapx(mapx)
                .mapy(mapy)
                .link(link)
                .title(title)
                .address(address)
                .roadAddress(roadAddress)
                .category(category)
                .telephone(telephone)
                .description(description)
                .build();
    }
}
