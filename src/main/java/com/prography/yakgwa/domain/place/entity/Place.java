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

    public boolean isSamePlace(String title, String mapx, String mapy) {
        return isSameTitle(title) && isSameX(mapx) && isSameY(mapy);
    }

    private  boolean isSameY(String compareY){
        return mapy.equals(compareY);
    }
    private boolean isSameX(String compareX){
        return mapx.equals(compareX);
    }
    private boolean isSameTitle(String compareTitle){
        return title.equals(compareTitle);
    }
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
