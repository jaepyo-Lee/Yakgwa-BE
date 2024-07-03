package com.prography.yakgwa.domain.vote.controller.req;

import com.prography.yakgwa.domain.vote.service.req.EnableTimeRequestDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EnableTimeRequest {
    private List<EnableTImeVote> enableTImes;

    @Getter
    private static class EnableTImeVote {
        private LocalDateTime enableTime;
    }

    public EnableTimeRequestDto toRequestDto(){
        List<LocalDateTime> enableTimes = this.enableTImes.stream().map(EnableTImeVote::getEnableTime).toList();
        return EnableTimeRequestDto.builder().enableTimes(enableTimes).build();
    }
}
