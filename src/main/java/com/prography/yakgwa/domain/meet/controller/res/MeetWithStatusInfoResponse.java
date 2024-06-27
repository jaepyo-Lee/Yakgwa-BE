package com.prography.yakgwa.domain.meet.controller.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.TimeVote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MeetWithStatusInfoResponse {
    private List<MeetInfoWithStatus> meetInfosWithStatus;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    private static class MeetInfoWithStatus {
        private MeetStatus meetStatus;
        private MeetInfo meetInfo;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    private static class MeetInfo {
        private String meetThemeName;
        private LocalDateTime meetDateTime;
        private String placeName;
        private String meetTitle;
        private Long meetId;
    }

    public static MeetWithStatusInfoResponse of(List<MeetWithVoteAndStatus> list) {
        return MeetWithStatusInfoResponse.builder().meetInfosWithStatus(
                list.stream().map(meetWithVoteAndStatus -> {
                    PlaceVote placeVote = meetWithVoteAndStatus.getPlaceVote();
                    TimeVote timeVote = meetWithVoteAndStatus.getTimeVote();
                    Meet meet = meetWithVoteAndStatus.getMeet();

                    return MeetInfoWithStatus.builder()
                            .meetStatus(meetWithVoteAndStatus.getMeetStatus())
                            .meetInfo(MeetInfo.builder()
                                    .meetDateTime(timeVote == null ? null : timeVote.getTime())
                                    .meetId(meet.getId())
                                    .meetThemeName(meet.getMeetTheme().getName())
                                    .placeName(placeVote == null || placeVote.getPlace() == null ? null : placeVote.getPlace().getTitle())
                                    .meetTitle(meet.getTitle())
                                    .build())
                            .build();
                }).toList()
        ).build();
    }
}
