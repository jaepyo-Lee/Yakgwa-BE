package com.prography.yakgwa.domain.meet.impl.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ConfirmTimeDto {
    private LocalDateTime meetTime;
}
