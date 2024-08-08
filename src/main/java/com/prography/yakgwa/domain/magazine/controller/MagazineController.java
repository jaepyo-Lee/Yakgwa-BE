package com.prography.yakgwa.domain.magazine.controller;

import com.prography.yakgwa.domain.magazine.controller.docs.MagazineApi;
import com.prography.yakgwa.domain.magazine.controller.res.PagingMagazineResponse;
import com.prography.yakgwa.domain.magazine.service.MagazineService;
import com.prography.yakgwa.domain.magazine.service.res.MagazineInfoResponseDto;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api.base}")
public class MagazineController implements MagazineApi {
    private final MagazineService magazineService;

    @GetMapping(value = "/magazines")
    public SuccessResponse<PagingMagazineResponse> findAllByPaging(@Parameter(description = "페이지번호는 0부터 시작해주세요!") @RequestParam(name = "pageNum") int pageNum) {
        MagazineInfoResponseDto responseDto = magazineService.findPaging(pageNum);
        return new SuccessResponse<>(PagingMagazineResponse.of(responseDto));
    }
}
