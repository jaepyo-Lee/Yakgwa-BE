package com.prography.yakgwa.domain.common.util.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TokenSet {
    @Schema(description = "api호출시 필요한 accessToken")
    private String accessToken;
    @Schema(description = "토큰 재발급시 필요한 refershToken")
    private String refreshToken;

    public TokenSet(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenSet ofBearer(String accessToken, String refreshToken){
        return new TokenSet(accessToken, refreshToken);
    }
}

