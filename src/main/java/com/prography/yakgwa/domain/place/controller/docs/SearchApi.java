package com.prography.yakgwa.domain.place.controller.docs;

import com.prography.yakgwa.domain.place.service.dto.PlaceInfoWithUserLike;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Search", description = "장소 검색 API입니다.")
public interface SearchApi {

    @Operation(summary = "장소검색 API", description = "검색시 좋아요 여부 함께 반환")
    SuccessResponse<List<PlaceInfoWithUserLike>> search(
            @Parameter(required = true,
                    description = "검색하고자하는 장소입력")
            @RequestParam("search") String search,
                                       @AuthenticationPrincipal CustomUserDetail user) throws Exception;
}
