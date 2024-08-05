package com.prography.yakgwa.domain.auth.service.response;

import com.prography.yakgwa.domain.auth.controller.response.ReissueTokenSetResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReissueTokenSetResponseDtoTest {
    @Test
    void toResponse테스트() {
        // given
        ReissueTokenSetResponseDto responseDto = ReissueTokenSetResponseDto.builder().reissueRefreshToken("refresh").reissueAccessToken("access").build();

        // when
        System.out.println("=====Logic Start=====");

        ReissueTokenSetResponse response = responseDto.toResponse();

        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(responseDto.getReissueAccessToken()).isEqualTo(response.getTokenSet().getAccessToken()),
                ()-> assertThat(responseDto.getReissueRefreshToken()).isEqualTo(response.getTokenSet().getRefreshToken()));
    }
}