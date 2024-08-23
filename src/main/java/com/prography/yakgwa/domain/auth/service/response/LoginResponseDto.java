package com.prography.yakgwa.domain.auth.service.response;

import com.prography.yakgwa.domain.user.entity.Role;
import com.prography.yakgwa.domain.common.util.jwt.TokenSet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    @Schema(description = "인증 jwt토큰")
    private TokenSet tokenSet;
    @Schema(description = "로그인한 사용자가 신규 회원인지 확인하는 값")
    private Boolean isNew;
    @Schema(description = "사용자 권한")
    private Role role;
}
