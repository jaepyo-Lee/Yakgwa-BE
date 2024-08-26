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
    @Schema(description = "사용자 권한")
    private Role role;
}
