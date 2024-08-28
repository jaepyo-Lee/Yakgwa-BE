package com.prography.yakgwa.domain.auth.controller.response;

import com.prography.yakgwa.domain.common.util.jwt.TokenSet;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReissueTokenSetResponse {
    private TokenSet tokenSet;
}
