package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.vote.controller.req.PlaceSlotAppendRequest;
import com.prography.yakgwa.domain.vote.controller.res.NewPlaceSlotResponse;
import com.prography.yakgwa.domain.vote.controller.res.AllPlaceSlotOfMeetResponse;
import com.prography.yakgwa.domain.vote.controller.res.PlaceSlotOfMeet;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.service.PlaceSlotService;
import com.prography.yakgwa.domain.vote.service.res.PlaceSlotWithUserResponse;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class PlaceSlotController implements PlaceSlotApi {
    private final PlaceSlotService placeSlotService;

    @PostMapping("/meets/{meetId}/placeslots")
    public SuccessResponse<NewPlaceSlotResponse> appendPlaceSlotFrom(@PathVariable("meetId") Long meetId,
                                                                     @RequestBody @Valid PlaceSlotAppendRequest placeSlotAppendRequest) {
        PlaceSlot placeSlot = placeSlotService.appendPlaceSlotFrom(meetId, placeSlotAppendRequest.getPlaceInfo());
        Place place = placeSlot.getPlace();
        return new SuccessResponse<>(NewPlaceSlotResponse.of(place));
    }

    @GetMapping("/meets/{meetId}/placeslots")
    public SuccessResponse<AllPlaceSlotOfMeetResponse> findPlaceSlotFrom(@PathVariable("meetId") Long meetId) {
        List<PlaceSlotWithUserResponse> slotInMeet = placeSlotService.findPlaceSlotFrom(meetId);
        List<PlaceSlotOfMeet> placeSlotOfMeets = slotInMeet.stream()
                .map(placeSlotWithUserResponse -> PlaceSlotOfMeet.of(placeSlotWithUserResponse.getPlaceSlot(), placeSlotWithUserResponse.getUsers()))
                .toList();
        return new SuccessResponse<>(AllPlaceSlotOfMeetResponse.of(placeSlotOfMeets));
    }
}
