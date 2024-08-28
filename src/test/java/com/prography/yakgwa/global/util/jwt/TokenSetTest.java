package com.prography.yakgwa.global.util.jwt;

import com.prography.yakgwa.domain.common.util.jwt.TokenSet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TokenSetTest {
    @Test
    void ofBearer테스트() {
        // given
        // when
        System.out.println("=====Logic Start=====");

        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        TokenSet tokenSet = TokenSet.ofBearer(accessToken, refreshToken);

        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(tokenSet.getAccessToken()).isEqualTo(accessToken),
                ()-> assertThat(tokenSet.getRefreshToken()).isEqualTo(refreshToken));
    }
}