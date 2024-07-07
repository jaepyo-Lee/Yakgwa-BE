package com.prography.yakgwa.domain.meet.controller.req;

import com.prography.yakgwa.domain.meet.service.dto.VoteDateDto;
import com.prography.yakgwa.domain.meet.service.req.MeetCreateRequestDto;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreateMeetRequest {
    private MeetInfo meetInfo;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    private static class MeetInfo {
        private String meetTitle;
        private Long meetThemeId;

        //null이라면 장소투표로
        private PlaceInfoDto placeInfo;

        // 해당값이 있다면 투표, null이라면 확정
        private VoteDate voteDate;

        // 해당값이 있다면 확정, null이면 투표
        private LocalDateTime meetTime;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    private static class VoteDate {
        private LocalDate startVoteDate;
        private LocalDate endVoteDate;
    }

    public MeetCreateRequestDto toRequestDto(Long creatorId) {
        return MeetCreateRequestDto.builder()
                .creatorId(creatorId)
                .title(meetInfo.meetTitle)
                .placeInfo(meetInfo.placeInfo)
                .voteDateDto(meetInfo.voteDate != null ? VoteDateDto.builder()
                        .endVoteDate(meetInfo.voteDate.endVoteDate)
                        .startVoteDate(meetInfo.voteDate.startVoteDate)
                        .build() : null)
                .meetTime(meetInfo.meetTime)
                .meetThemeId(meetInfo.meetThemeId)
                .build();
    }
}
