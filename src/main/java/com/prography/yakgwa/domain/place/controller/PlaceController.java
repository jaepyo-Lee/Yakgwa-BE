package com.prography.yakgwa.domain.place.controller;

import com.prography.yakgwa.domain.place.controller.req.LikePlaceRequest;
import com.prography.yakgwa.domain.place.service.PlaceService;
import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class PlaceController implements PlaceApi {
    private final PlaceService placeService;

    @PostMapping("/place/like")
    public SuccessResponse<Boolean> likePlace(@RequestParam("like") boolean like,
                                              @AuthenticationPrincipal CustomUserDetail user,
                                              @RequestBody LikePlaceRequest likePlaceRequest) {
        placeService.decideLike(user.getUserId(), like, likePlaceRequest);
        return new SuccessResponse<>(like);
    }
}
