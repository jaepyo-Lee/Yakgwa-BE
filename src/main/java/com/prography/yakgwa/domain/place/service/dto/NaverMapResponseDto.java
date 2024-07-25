package com.prography.yakgwa.domain.place.service.dto;

import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class NaverMapResponseDto {
    private String lastBuildDate;
    private Integer total;
    private Integer start;
    private Integer display;
    private List<PlaceInfoDto> items;
}
