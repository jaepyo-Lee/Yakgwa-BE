package com.prography.yakgwa.domain.place.controller;

import com.prography.yakgwa.domain.place.service.SearchService;
import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class SearchController implements SearchApi{
    private final SearchService service;

    @GetMapping("/search")
    public SuccessResponse<List<PlaceInfoWithUserLike>> search(@RequestParam("search") String search,
                                                               @AuthenticationPrincipal CustomUserDetail user) {
        return new SuccessResponse<>(service.search(search, user.getUserId()));
    }
}
