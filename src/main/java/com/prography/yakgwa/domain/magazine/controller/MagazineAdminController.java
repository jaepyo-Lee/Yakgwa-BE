package com.prography.yakgwa.domain.magazine.controller;

import com.prography.yakgwa.domain.magazine.controller.req.CreateMagazineRequest;
import com.prography.yakgwa.domain.magazine.service.MagazineService;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api.admin}")
public class MagazineController {
    private final MagazineService magazineService;
    @PostMapping("/magazines")
    public SuccessResponse create(@RequestPart CreateMagazineRequest createMagazineRequest,
                                  @RequestPart List<MultipartFile> multipartFiles,
                                  @AuthenticationPrincipal CustomUserDetail user) {
        magazineService.create(createMagazineRequest.toRequestDto(user.getUserId()),multipartFiles);
        return null;
    }
}
