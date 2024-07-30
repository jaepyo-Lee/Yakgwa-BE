package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.impl.dto.MeetWriteDto;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.meet.impl.dto.ConfirmPlaceDto;
import com.prography.yakgwa.domain.meet.impl.dto.ConfirmTimeDto;
import com.prography.yakgwa.global.format.exception.meet.MeetTimeParamDuplicationException;
import com.prography.yakgwa.global.format.exception.meet.NotFoundThemeException;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@ImplService
public class MeetWriter {
    private final MeetJpaRepository meetJpaRepository;
    private final MeetThemeJpaRepository meetThemeJpaRepository;

    public Meet write(MeetWriteDto writeDto) {
        if ((writeDto.getMeetTime() == null && writeDto.getPeriod() == null) ||
                (writeDto.getMeetTime() != null && writeDto.getPeriod() != null)) {
            throw new MeetTimeParamDuplicationException();
        }
        MeetTheme meetTheme = meetThemeJpaRepository.findById(writeDto.getMeetThemeId())
                .orElseThrow(NotFoundThemeException::new);

        VotePeriod votePeriod = null;
        if (writeDto.getPeriod() != null) {
            votePeriod = VotePeriod.builder()
                    .startDate(writeDto.getPeriod().getStartDate())
                    .endDate(writeDto.getPeriod().getEndDate())
                    .build();
        }

        Meet meet = Meet.builder()
                .description(writeDto.getDescription())
                .title(writeDto.getTitle())
                .period(votePeriod)
                .meetTheme(meetTheme)
                .validInviteHour(24)
                .build();

        Meet saveMeet = meetJpaRepository.save(meet);
        return saveMeet;
    }
}
