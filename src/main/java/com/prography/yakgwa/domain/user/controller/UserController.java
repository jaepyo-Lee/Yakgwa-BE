package com.prography.yakgwa.domain.user.controller;

import com.prography.yakgwa.domain.user.controller.docs.UserApi;
import com.prography.yakgwa.domain.user.controller.res.UserInfoResponse;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.service.UserService;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.api.base}")
public class UserController implements UserApi {
    private final UserService userService;

    @ApiResponse(responseCode = "200", description = "성공시 따로 반환값 없습니다! 필요한 반환값 있으면 말씀해주세요!")
    @PatchMapping(value = "/user/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse modifyImage(@RequestPart("userImage") MultipartFile userImage,
                                       @AuthenticationPrincipal CustomUserDetail userDetail) throws IOException {
        userService.modify(userImage,userDetail.getUserId());
        return SuccessResponse.ok();
    }

    @GetMapping("/user")
    public SuccessResponse<UserInfoResponse> info(@AuthenticationPrincipal CustomUserDetail userDetail){
        User user = userService.find(userDetail.getUserId());
        return new SuccessResponse<>(UserInfoResponse.of(user));
    }
    @PatchMapping("/user/fcm")
    public SuccessResponse updateFcm(@AuthenticationPrincipal CustomUserDetail userDetail,
                                     @RequestParam(value = "newFcmToken") String newFcmToken) {
        userService.updateFcm(userDetail.getUserId(), newFcmToken);
        return SuccessResponse.ok("토큰갱신성공");
    }
}
