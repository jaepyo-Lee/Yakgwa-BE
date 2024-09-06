package com.prography.yakgwa.domain.place.controller.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.place.controller.req.LikePlaceRequest;
import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Place", description = "장소 API입니다.")
public interface PlaceApi {
    @Operation(summary = "나의 장소 등록 API", description = "나의 장소 등록시 사용")
    SuccessResponse<Boolean> likePlace(@Parameter(required = true,
            description = "해당 장소를 등록할시 true, 등록 취소시 false 보내주세요")
                                       @RequestParam("like") boolean like,
                                       @AuthenticationPrincipal CustomUserDetail user,
                                       @RequestBody LikePlaceRequest likePlaceRequest) throws JsonProcessingException;

    @Operation(summary = "나의 장소 조회 API")
    SuccessResponse<List<PlaceInfoWithUserLike>> findLikePlace(@AuthenticationPrincipal CustomUserDetail user);
}
