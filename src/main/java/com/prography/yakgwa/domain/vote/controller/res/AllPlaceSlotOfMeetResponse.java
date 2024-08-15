package com.prography.yakgwa.domain.vote.controller.res;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AllPlaceSlotOfMeetResponse {

    @Schema(description = "각 장소후보의 정보")
    private List<PlaceSlotOfMeet> placeSlotOfMeet;

    public static AllPlaceSlotOfMeetResponse of(List<PlaceSlotOfMeet>placeSlotOfMeet){
        return AllPlaceSlotOfMeetResponse.builder()
                .placeSlotOfMeet(placeSlotOfMeet)
                .build();
    }
}
