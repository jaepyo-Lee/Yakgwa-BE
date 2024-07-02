package com.prography.yakgwa.domain.vote.controller.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class TimeVoteInfoWithStatusResponse {
    private MeetStatus meetStatus;
    private List<VoteTimeInfo> timeInfos;

    @Getter
    @Builder
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
