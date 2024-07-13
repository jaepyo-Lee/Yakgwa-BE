package com.prography.yakgwa.domain.meet.impl.dto;

import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.service.req.MeetCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MeetWriteDto {
    private String title;
    private Long meetThemeId;
    private String description;
    //해당 값이 있다면 투표,null이면 확정
    private VotePeriod period;
    //해당값이 있다면 확정,null이면 투표
    private LocalDateTime meetTime;

    public static MeetWriteDto of(MeetCreateRequestDto requestDto) {
        VotePeriod votePeriod = null;
        if (requestDto.getVoteDateDto() != null) {
            votePeriod = VotePeriod.builder()
                    .startDate(requestDto.getVoteDateDto().getStartVoteDate())
                    .endDate(requestDto.getVoteDateDto().getEndVoteDate())
                    .build();
        }

        return MeetWriteDto.builder()
                .meetThemeId(requestDto.getMeetThemeId())
                .period(votePeriod)
                .title(requestDto.getTitle())
                .meetTime(requestDto.getMeetTime())
                .build();
    }

}
