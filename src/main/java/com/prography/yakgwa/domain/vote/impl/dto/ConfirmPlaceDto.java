package com.prography.yakgwa.domain.vote.impl.dto;

import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ConfirmPlaceDto {
    private boolean confirmPlace;

    //null이라면 장소투표로
    private List<PlaceInfoDto> placeInfo;
}
