package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.vote.controller.req.PlaceSlotAppendRequest;
import com.prography.yakgwa.domain.vote.controller.res.AllPlaceSlotOfMeetResponse;
import com.prography.yakgwa.domain.vote.controller.res.NewPlaceSlotResponse;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Meet", description = "모임관련 API입니다.")
public interface PlaceSlotApi {

    @Operation(summary = "투표를 위한 모임의 장소투표후보지 추가 API", description = "")
    SuccessResponse<NewPlaceSlotResponse> appendPlaceSlotFrom(@PathVariable("meetId") Long meetId,
                                                              @RequestBody PlaceSlotAppendRequest placeSlotAppendRequest);

    @Operation(summary = "투표를 위한 모임의 장소후보 정보조회 API", description = "")
    SuccessResponse<AllPlaceSlotOfMeetResponse> findPlaceSlotFrom(@PathVariable("meetId") Long meetId);
}
