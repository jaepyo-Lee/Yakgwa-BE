package com.prography.yakgwa.domain.meet.service.req;

import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.impl.dto.MeetWriteDto;
import com.prography.yakgwa.domain.meet.service.dto.VoteDateDto;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.vote.impl.dto.ConfirmPlaceDto;
import com.prography.yakgwa.domain.vote.impl.dto.ConfirmTimeDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MeetCreateRequestDto {
    private Long creatorId;

    private String title;
    private Long meetThemeId;

    private boolean confirmPlace;

    //null이라면 장소투표로
    private List<PlaceInfoDto> placeInfo;

    //해당 값이 있다면 투표,null이면 투표
    private VoteDateDto voteDateDto;

    //해당값이 있다면 확정,null이면 투표
    private LocalDateTime meetTime;

    public ConfirmPlaceDto toConfirmPlaceDto(){
        return ConfirmPlaceDto.builder()
                .confirmPlace(confirmPlace)
                .placeInfo(placeInfo)
                .build();
    }
    public ConfirmTimeDto toConfirmTimeDto(){
        return ConfirmTimeDto.builder()
                .meetTime(meetTime)
                .build();
    }
    public MeetWriteDto toMeetWriteDto(){
        VotePeriod votePeriod = null;
        if (voteDateDto != null) {
            votePeriod = VotePeriod.builder()
                    .startDate(voteDateDto.getStartVoteDate())
                    .endDate(voteDateDto.getEndVoteDate())
                    .build();
        }
        return MeetWriteDto.builder()
                .period(votePeriod)
                .meetTime(meetTime)
                .meetThemeId(meetThemeId)
                .build();
    }



}
