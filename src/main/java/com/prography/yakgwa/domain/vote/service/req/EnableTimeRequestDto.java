package com.prography.yakgwa.domain.vote.service.req;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class EnableTimeRequestDto {
    private Set<LocalDateTime> enableTimes;
}
