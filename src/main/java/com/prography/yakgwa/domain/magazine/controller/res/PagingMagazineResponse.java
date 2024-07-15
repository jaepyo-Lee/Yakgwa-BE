package com.prography.yakgwa.domain.magazine.controller.res;

import com.prography.yakgwa.domain.magazine.entity.Image;
import com.prography.yakgwa.domain.magazine.entity.ImageType;
import com.prography.yakgwa.domain.magazine.service.res.MagazineInfoEntity;
import com.prography.yakgwa.domain.magazine.service.res.MagazineInfoResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class PagingMagazineResponse {
    @Schema(description = "현재 페이지 번호")
    private int page;
    @Schema(description = "현재페이지의 원소개수")
    private int size;
    @Schema(description = "전체페이지수")
    private int totalPages;
    @Schema(description = "전체원소수")
    private long totalElements;
    @Schema(description = "현재 페이지 원소")
    private List<MagazineInfo> magazineInfo;

    @Builder
    @Getter
    @Schema(name = "PagingMagazineResponse-MagazineInfo")
    private static class MagazineInfo {
        private String title;
        private String thumbnailUrl;
        private String contentsUrl;
    }

    public static PagingMagazineResponse of(MagazineInfoResponseDto responseDto) {
        List<MagazineInfo> magazineInfos = new ArrayList<>();
        for (MagazineInfoEntity magazineInfoEntity : responseDto.getMagazineInfo()) {
            MagazineInfo.MagazineInfoBuilder infoBuilder = MagazineInfo.builder().title(magazineInfoEntity.getMagazine().getTitle());
            for (Image image : magazineInfoEntity.getImages()) {
                if (image.getType() == ImageType.THUMBNAIL) {
                    infoBuilder.thumbnailUrl(image.getUrl());
                } else {
                    infoBuilder.contentsUrl(image.getUrl());
                }
            }
            magazineInfos.add(infoBuilder.build());
        }

        return PagingMagazineResponse.builder()
                .page(responseDto.getPage())
                .totalElements(responseDto.getTotalElements())
                .size(responseDto.getSize())
                .totalPages(responseDto.getTotalPages())
                .magazineInfo(magazineInfos)
                .build();
    }
}
