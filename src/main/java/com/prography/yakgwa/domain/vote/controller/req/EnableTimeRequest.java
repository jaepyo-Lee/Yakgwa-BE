package com.prography.yakgwa.domain.vote.controller.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prography.yakgwa.domain.vote.service.req.EnableTimeRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "모임 참여가 가능한 시간<br>" +
        "취소한 정보외의 가능하다 표시한 모든 시간 보내주세요!<br>" +
        "이전에 이미 등록한 시간도 보내주세요!")
@Getter
public class EnableTimeRequest {
    @Schema(description = "가능한 시간<br>" +
            "범위가 아닌 각 시간을 리스트로 보내주세요!")
    @NotNull(message = "참여가능한 시간을 보내주세요.")
    private List<EnableTImeVote> enableTImes;

    @Getter
    @Schema(name = "EnableTimeRequest-EnableTImeVote")
    private static class EnableTImeVote {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        @Schema(example = "2024-07-16 20:00")
        private LocalDateTime enableTime;
    }

    public EnableTimeRequestDto toRequestDto() {
        List<LocalDateTime> enableTimes = this.enableTImes.stream().map(EnableTImeVote::getEnableTime).toList();
        return EnableTimeRequestDto.builder().enableTimes(enableTimes).build();
    }
}
