package com.prography.yakgwa.domain.vote.controller.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prography.yakgwa.domain.vote.service.time.req.EnableTimeRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "모임 참여가 가능한 시간<br>" +
        "취소한 정보외의 가능하다 표시한 모든 시간 보내주세요!<br>" +
        "이전에 이미 등록한 시간도 보내주세요!")
@Getter
public class EnableTimeRequest {
    @Schema(description = "가능한 시간<br>" +
            "범위가 아닌 각 시간을 리스트로 보내주세요!")
    @NotNull(message = "참여가능한 시간을 보내주세요.")
    private List<EnableTImeVote> enableTimes;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(name = "EnableTimeRequest-EnableTImeVote")
    private static class EnableTImeVote {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        @Schema(example = "2024-07-16 20:00")
        private LocalDateTime enableTime;
    }

    public EnableTimeRequestDto toRequestDto() {
        Set<LocalDateTime> enableTimes = this.enableTimes.stream().map(EnableTImeVote::getEnableTime).collect(Collectors.toSet());
        return EnableTimeRequestDto.builder().enableTimes(enableTimes).build();
    }

    public static EnableTimeRequest of(List<LocalDateTime> times) {
        return EnableTimeRequest.builder().enableTimes(times.stream()
                        .map(time -> EnableTImeVote.builder().enableTime(time).build())
                        .collect(toList()))
                .build();
    }
}
