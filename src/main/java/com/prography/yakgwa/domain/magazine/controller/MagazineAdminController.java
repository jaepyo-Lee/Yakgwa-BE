package com.prography.yakgwa.domain.magazine.controller;

import com.prography.yakgwa.domain.magazine.controller.req.CreateMagazineRequest;
import com.prography.yakgwa.domain.magazine.controller.res.CreateMagazineResponse;
import com.prography.yakgwa.domain.magazine.entity.Magazine;
import com.prography.yakgwa.domain.magazine.service.MagazineService;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api.admin}")
public class MagazineAdminController implements MagazineAdminApi {
    private final MagazineService magazineService;

    @PostMapping(value = "/magazines", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<CreateMagazineResponse> create(@RequestPart(name = "createMagazineRequest") @Valid CreateMagazineRequest createMagazineRequest,
                                                          @RequestPart(name = "thumbnail") MultipartFile thumbnail,
                                                          @RequestPart(name = "content") MultipartFile content,
                                                          @AuthenticationPrincipal CustomUserDetail user) {
        Magazine magazine = magazineService.create(createMagazineRequest.toRequestDto(user.getUserId()), thumbnail, content);
        return new SuccessResponse(new CreateMagazineResponse(magazine.getId()));
    }
}
