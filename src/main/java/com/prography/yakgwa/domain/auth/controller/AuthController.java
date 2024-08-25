package com.prography.yakgwa.domain.auth.controller;

import com.prography.yakgwa.domain.auth.controller.request.LoginRequest;
import com.prography.yakgwa.domain.auth.controller.response.ReissueTokenSetResponse;
import com.prography.yakgwa.domain.auth.service.AuthService;
import com.prography.yakgwa.domain.auth.service.request.LoginRequestDto;
import com.prography.yakgwa.domain.auth.service.response.LoginResponseDto;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import com.prography.yakgwa.global.util.HeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("${app.api.base}/auth")
@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;


    @PostMapping("/login")
    public SuccessResponse<LoginResponseDto> login(@RequestHeader("Authorization") String accessToken,
                                                   @Parameter @RequestBody LoginRequest loginRequest) {
        String token = HeaderUtil.parseBearer(accessToken);

        return new SuccessResponse(authService.login(LoginRequestDto.builder()
                .loginType(loginRequest.getLoginType())
                .token(token)
                .fcmToken(loginRequest.getFcmToken())
                .build()));
    }


    @GetMapping("/reissue")
    public SuccessResponse<ReissueTokenSetResponse> reissue(@RequestHeader(value = "refreshToken") String refreshToken) {
        ReissueTokenSetResponse reissueTokenSetResponse = authService.reissue(refreshToken).toResponse();
        return new SuccessResponse(reissueTokenSetResponse);
    }

    @PostMapping("/logout")
    public SuccessResponse logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return SuccessResponse.ok();
    }

    @PostMapping("/signout")
    public SuccessResponse signout(@AuthenticationPrincipal CustomUserDetail userDetail,
                                   @RequestHeader(value = "Authorization") String Authorization) {
        authService.signout(userDetail.getUserId(), Authorization);
        return SuccessResponse.ok("탈퇴되었습니다.");
    }
}
