package com.prography.yakgwa.domain.auth.service.request;

import com.prography.yakgwa.domain.user.entity.AuthType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {
    private String token;
    private AuthType loginType;
    private String fcmToken;
    private String baseImage;
}
