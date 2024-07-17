package com.prography.yakgwa.domain.user.controller;

import com.prography.yakgwa.domain.user.controller.res.UserInfoResponse;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "User", description = "사용자관련 API입니다.")
public interface UserApi {
    @Operation(summary = "사용자의 사진 변경 API", description = "")
    SuccessResponse modifyImage(@RequestPart("userImage") MultipartFile userImage,
                                @AuthenticationPrincipal CustomUserDetail userDetail) throws IOException;

    @Operation(summary = "사용자정보 조회 API", description = "")
    SuccessResponse<UserInfoResponse> info(@AuthenticationPrincipal CustomUserDetail userDetail);
}
