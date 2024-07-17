package com.prography.yakgwa.domain.magazine.controller;

import com.prography.yakgwa.domain.magazine.controller.req.CreateMagazineRequest;
import com.prography.yakgwa.domain.magazine.controller.req.ModifyMagazineOpenStateRequest;
import com.prography.yakgwa.domain.magazine.controller.res.CreateMagazineResponse;
import com.prography.yakgwa.domain.magazine.controller.res.ModifyMagazineOpenStateResponse;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Magazine_Admin", description = "[관리자] 매거진과 관련된 API입니다.")
public interface MagazineAdminApi {
    @Operation(summary = "매거진생성 API", description = "매거진 생성 API 관리자 권한의 사용자만 허용됌")
    SuccessResponse<CreateMagazineResponse> create(@RequestPart CreateMagazineRequest createMagazineRequest,
                                                   @RequestPart MultipartFile thumbnail,
                                                   @RequestPart MultipartFile content,
                                                   @AuthenticationPrincipal CustomUserDetail user) throws IOException;

    @Operation(summary = "매거진 공개여부 수정 API", description = "매거진 공개여부 수정 API")
    SuccessResponse<ModifyMagazineOpenStateResponse> modifyOpen(@RequestBody ModifyMagazineOpenStateRequest modifyMagazineOpen,
                                                                @AuthenticationPrincipal CustomUserDetail user);
}
