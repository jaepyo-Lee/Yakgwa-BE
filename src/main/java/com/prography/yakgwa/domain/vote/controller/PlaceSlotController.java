package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.vote.controller.req.PlaceSlotAppendRequest;
import com.prography.yakgwa.domain.vote.controller.res.NewPlaceSlotResponse;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.service.PlaceSlotService;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class PlaceSlotController implements PlaceSlotApi{
    private final PlaceSlotService placeSlotService;

    @PostMapping("/meets/{meetId}/placeslots")
    public SuccessResponse<NewPlaceSlotResponse> appendPlaceSlotOfMeet(@PathVariable("meetId") Long meetId,
                                                                       @RequestBody @Valid PlaceSlotAppendRequest placeSlotAppendRequest) {
        PlaceSlot placeSlot = placeSlotService.appendSlotInMeet(meetId, placeSlotAppendRequest.getPlaceInfo());
        Place place = placeSlot.getPlace();
        return new SuccessResponse<>(NewPlaceSlotResponse.of(place));
    }
}
