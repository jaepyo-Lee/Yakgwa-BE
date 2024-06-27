package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.impl.dto.MeetWriteDto;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ImplService
public class MeetWriter {
    private final MeetJpaRepository meetJpaRepository;
    private final MeetThemeReader meetThemeReader;

    public Meet write(MeetWriteDto writeDto) {
        MeetTheme meetTheme = meetThemeReader.readByRef(writeDto.getMeetThemeId());
        Meet meet = Meet.builder()
                .title(writeDto.getTitle())
                .period(writeDto .getPeriod() != null ?
                        VotePeriod.builder()
                                .endDate(writeDto.getPeriod().getEndDate())
                                .startDate(writeDto.getPeriod().getStartDate())
                                .build()
                        : null)
                .meetTheme(meetTheme)
                .validInviteHour(24)
                .build();
        return meetJpaRepository.save(meet);
    }
}
