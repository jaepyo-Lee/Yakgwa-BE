package com.prography.yakgwa.domain.magazine.controller;

import com.prography.yakgwa.domain.magazine.controller.res.CreateMagazineResponse;
import com.prography.yakgwa.domain.magazine.controller.res.PagingMagazineResponse;
import com.prography.yakgwa.global.format.success.SuccessResponse;

public interface MagazineApi {
    SuccessResponse<PagingMagazineResponse> findAllByPaging(int pageNum);
}
