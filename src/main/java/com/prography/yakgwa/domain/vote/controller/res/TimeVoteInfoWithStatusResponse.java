package com.prography.yakgwa.domain.vote.controller.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "사용자가 투표한 장소목록<br>" +
        "모임의 상태에 따라 placeInfos의 경우 안나올수 있음<br>" +
        "meetStatus : CONFIRM , placeInfo : 확정된 장소의 정보<br>" +
        "meetStatus : VOTE , placeInfo : 사용자의 투표정보<br>" +
        "meetStatus : BEFORE_VOTE , placeInfo : 아무값 안나감")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class TimeVoteInfoWithStatusResponse {
    @Schema(description = "투표 상태")
    private MeetStatus meetStatus;
    @Schema(description = "상황에 따른 시간 정보")
    private List<VoteTimeInfo> timeInfos;

    @Getter
    @Builder
    @Schema(name = "TimeVoteInfoWithStatusResponse-VoteTimeInfo")
    private static class VoteTimeInfo {
        private Long timeId;
        private LocalDateTime voteTime;
    }

    public static TimeVoteInfoWithStatusResponse of(MeetStatus meetStatus, List<TimeSlot> timeSlots) {
        return TimeVoteInfoWithStatusResponse.builder()
                .meetStatus(meetStatus)
                .timeInfos(timeSlots.isEmpty() ? null : timeSlots.stream()
                        .map(timeVote -> VoteTimeInfo.builder()
                                .timeId(timeVote.getId())
                                .voteTime(timeVote.getTime())
                                .build())
                        .toList())
                .build();
    }
}
