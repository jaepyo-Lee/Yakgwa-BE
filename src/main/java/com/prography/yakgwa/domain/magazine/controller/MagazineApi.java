package com.prography.yakgwa.domain.magazine.controller;

import com.prography.yakgwa.domain.magazine.controller.res.CreateMagazineResponse;
import com.prography.yakgwa.domain.magazine.controller.res.PagingMagazineResponse;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Magazine_User", description = "[사용자]매거진과 관련된 API입니다.")
public interface MagazineApi {

    @Operation(summary = "매거진 조회 API", description = "페이지네이션적용, 모든 사용자 접근가능")
    SuccessResponse<PagingMagazineResponse> findAllByPaging(int pageNum);
}
