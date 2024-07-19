package com.prography.yakgwa.domain.vote.controller.req;

import com.prography.yakgwa.domain.vote.service.req.EnableTimeRequestDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EnableTimeRequestTest {
    @Test
    void dto로변환시set으로중복시간삭제하는테스트() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<LocalDateTime> times = List.of(now, now, now, now);
        EnableTimeRequest enableTimeRequest = EnableTimeRequest.of(times);
        // when
        System.out.println("=====Logic Start=====");

        EnableTimeRequestDto requestDto = enableTimeRequest.toRequestDto();

        System.out.println("=====Logic End=====");
        // then
        assertThat(requestDto.getEnableTimes().size()).isEqualTo(1);

    }
}