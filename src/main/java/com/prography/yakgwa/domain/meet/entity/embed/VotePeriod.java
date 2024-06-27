package com.prography.yakgwa.domain.meet.entity.embed;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Embeddable
public class VotePeriod {
    private LocalDate startDate;
    private LocalDate endDate;
}
