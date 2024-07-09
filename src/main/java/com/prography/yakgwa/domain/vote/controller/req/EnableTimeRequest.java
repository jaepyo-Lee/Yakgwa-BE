package com.prography.yakgwa.domain.vote.controller.req;

import com.prography.yakgwa.domain.vote.service.req.EnableTimeRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "모임 참여가 가능한 시간 목록")
@Getter
public class EnableTimeRequest {
    @Schema(description = "가능한 시간<br>" +
            "범위가 아닌 시간별로 보내주세요!")
    private List<EnableTImeVote> enableTImes;

    @Getter
    private static class EnableTImeVote {
        private LocalDateTime enableTime;
    }

    public EnableTimeRequestDto toRequestDto() {
        List<LocalDateTime> enableTimes = this.enableTImes.stream().map(EnableTImeVote::getEnableTime).toList();
        return EnableTimeRequestDto.builder().enableTimes(enableTimes).build();
    }
}
