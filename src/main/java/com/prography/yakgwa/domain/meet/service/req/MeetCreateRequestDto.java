package com.prography.yakgwa.domain.meet.service.req;

import com.prography.yakgwa.domain.meet.service.dto.VoteDateDto;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MeetCreateRequestDto {
    private Long creatorId;

    private String title;
    private Long meetThemeId;

    //null이라면 장소투표로
    private PlaceInfoDto placeInfo;

    //해당 값이 있다면 투표,null이면 투표
    private VoteDateDto voteDateDto;

    //해당값이 있다면 확정,null이면 투표
    private LocalDateTime meetTime;
}
