package com.prography.yakgwa.domain.place.entity.dto;

import com.prography.yakgwa.domain.place.entity.Place;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlaceInfoDto {
    @Schema(description = "장소명", example = "스타벅스 강남역점")
    private String title;
    @Schema(description = "링크", example = "www.starbucks.com")
    private String link;
    @Schema(description = "카테고리", example = "cafe")
    private String category;
    @Schema(description = "설명", example = "스타벅스")
    private String description;
    @Schema(description = "가게 전화번호", example = "02-xxx-xxx")
    private String telephone;
    @Schema(description = "주소", example = "도곡동 956 LG전자 강남R&D센터")
    private String address;
    @Schema(description = "도로주소", example = "서울특별시 강남구 강남대로 390")
    private String roadAddress;
    @Schema(description = "x좌표",example = "232")
    private String mapx;
    @Schema(description = "y좌표",example = "123")
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
