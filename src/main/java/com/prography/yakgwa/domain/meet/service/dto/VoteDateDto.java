package com.prography.yakgwa.domain.meet.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class VoteDateDto {
    private LocalDate startVoteDate;
    private LocalDate endVoteDate;
}
