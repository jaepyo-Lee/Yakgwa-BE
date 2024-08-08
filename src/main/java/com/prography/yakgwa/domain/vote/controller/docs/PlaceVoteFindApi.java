package com.prography.yakgwa.domain.vote.controller.docs;

import com.prography.yakgwa.domain.vote.controller.res.PlaceVoteInfoWithStatusResponse;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Vote", description = "투표관련 API입니다.")
public interface PlaceVoteFindApi {
    @Operation(summary = "모임의 사용자가 투표한 장소목록 조회 API", description = "")
    SuccessResponse<PlaceVoteInfoWithStatusResponse> placeInfoByMeetStatus(@AuthenticationPrincipal CustomUserDetail user,
                                                                           @PathVariable("meetId") Long meetId);
}
