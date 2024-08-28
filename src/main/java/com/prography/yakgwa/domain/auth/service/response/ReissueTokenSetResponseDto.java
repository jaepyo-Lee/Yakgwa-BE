package com.prography.yakgwa.domain.auth.service.response;

import com.prography.yakgwa.domain.auth.controller.response.ReissueTokenSetResponse;
import com.prography.yakgwa.domain.common.util.jwt.TokenSet;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReissueTokenSetResponseDto {
    private String reissueAccessToken;
    private String reissueRefreshToken;

    public ReissueTokenSetResponse toResponse() {
        TokenSet tokenSet = TokenSet.ofBearer(this.reissueAccessToken, this.reissueRefreshToken);
        return ReissueTokenSetResponse.builder()
                .tokenSet(tokenSet)
                .build();
    }
}
