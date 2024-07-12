package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.impl.dto.MeetWriteDto;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.vote.impl.PlaceVoteWriter;
import com.prography.yakgwa.domain.vote.impl.TimeVoteWriter;
import com.prography.yakgwa.domain.vote.impl.dto.ConfirmPlaceDto;
import com.prography.yakgwa.domain.vote.impl.dto.ConfirmTimeDto;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ImplService
public class MeetWriter {
    private final MeetJpaRepository meetJpaRepository;
    private final MeetThemeReader meetThemeReader;
    private final PlaceVoteWriter placeVoteWriter;
    private final TimeVoteWriter timeVoteWriter;

    public Meet write(MeetWriteDto writeDto, ConfirmPlaceDto confirmPlaceDto, ConfirmTimeDto confirmTimeDto) {
        if ((writeDto.getMeetTime() == null && writeDto.getPeriod() == null) ||
                (writeDto.getMeetTime() != null && writeDto.getPeriod() != null)) {
            throw new RuntimeException("시간은 투표 또는 확정중 한가지만 가능합니다!");
        }
        MeetTheme meetTheme = meetThemeReader.read(writeDto.getMeetThemeId());

        VotePeriod votePeriod = null;
        if (writeDto.getPeriod() != null) {
            votePeriod = VotePeriod.builder()
                    .startDate(writeDto.getPeriod().getStartDate())
                    .endDate(writeDto.getPeriod().getEndDate())
                    .build();
        }

        Meet meet = Meet.builder()
                .title(writeDto.getTitle())
                .period(votePeriod)
                .meetTheme(meetTheme)
                .validInviteHour(24)
                .build();

        Meet saveMeet = meetJpaRepository.save(meet);
        placeVoteWriter.decideConfirmAndWrite(saveMeet,confirmPlaceDto);
        timeVoteWriter.confirmAndWrite(saveMeet, confirmTimeDto);
        return saveMeet;
    }

}
