package com.prography.yakgwa.domain.auth.controller.request;

import com.prography.yakgwa.domain.user.entity.AuthType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginRequest {
    @Schema(description = "사용자가 로그인을 진행하는 플랫폼명")
    private AuthType loginType;
}
