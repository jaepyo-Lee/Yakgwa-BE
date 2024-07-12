package com.prography.yakgwa.domain.vote.impl.dto;

import com.prography.yakgwa.domain.meet.service.dto.VoteDateDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ConfirmTimeDto {
    private LocalDateTime meetTime;
}
