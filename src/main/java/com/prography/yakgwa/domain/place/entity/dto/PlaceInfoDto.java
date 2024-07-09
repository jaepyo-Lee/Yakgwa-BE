package com.prography.yakgwa.domain.place.entity.dto;

import com.prography.yakgwa.domain.place.entity.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "장소 정보")
@Getter
public class PlaceInfoDto {
    @Schema(description = "장소명")
    private String title;
    @Schema(description = "링크")
    private String link;
    @Schema(description = "카테고리")
    private String category;
    @Schema(description = "설명")
    private String description;
    @Schema(description = "가게 전화번호")
    private String telephone;
    @Schema(description = "주소")
    private String address;
    @Schema(description = "도로주소")
    private String roadAddress;
    @Schema(description = "x좌표")
    private String mapx;
    @Schema(description = "y좌표")
    private String mapy;

    public Place toEntity() {
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
