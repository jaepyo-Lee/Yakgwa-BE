package com.prography.yakgwa.domain.auth.controller;

import com.prography.yakgwa.domain.auth.controller.request.LoginRequest;
import com.prography.yakgwa.domain.auth.controller.response.ReissueTokenSetResponse;
import com.prography.yakgwa.domain.auth.service.AuthService;
import com.prography.yakgwa.domain.auth.service.request.LoginRequestDto;
import com.prography.yakgwa.domain.auth.service.response.LoginResponseDto;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import com.prography.yakgwa.global.util.HeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Login", description = "로그인과 관련된 토큰관련 API입니다.")
@RequiredArgsConstructor
@RequestMapping("${app.api.base}/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public SuccessResponse<LoginResponseDto> login(@Parameter(required = true,
            description = "1. 플랫폼 서버에서 가져온 인증토큰을 Bearer토큰으로 보내주세요!" + "\n" +
                    " 2. isNew필드를 통해 해당 사용자가 신규인지 기존회원인지 구별하여 사용자 추가 설정페이지로 넘어가질 구별하면 될것 같습니다!")
                                                   @RequestHeader("Authorization") String accessToken,
                                                   @Parameter @RequestBody LoginRequest loginRequest) {
        String token = HeaderUtil.parseBearer(accessToken);

        return new SuccessResponse(authService.login(LoginRequestDto.builder()
                .loginType(loginRequest.getLoginType())
                .token(token)
                .build()));
    }

    @Operation(summary = "토큰 재발급 API", description = "토큰 만료시 토큰 재발급을 위한 API")
    @GetMapping("/reissue")
    public SuccessResponse<ReissueTokenSetResponse> reissue(
            @Parameter(required = true,
                    description = "기존의 accessToken 만료되는 경우를 대비해 로그인시 발급한 refreshToken을 보내주세요.")
            @RequestHeader(value = "refreshToken") String refreshToken) {
        ReissueTokenSetResponse reissueTokenSetResponse = authService.reissue(refreshToken).toResponse();
        return new SuccessResponse(reissueTokenSetResponse);
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("/logout")
    public SuccessResponse logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return SuccessResponse.ok();
    }
}
