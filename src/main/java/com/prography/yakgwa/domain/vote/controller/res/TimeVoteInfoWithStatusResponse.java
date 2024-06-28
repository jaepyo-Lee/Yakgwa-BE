package com.prography.yakgwa.domain.vote.controller.res;

import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.TimeVote;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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

    public static TimeVoteInfoWithStatusResponse of(MeetStatus meetStatus, List<TimeVote> timeVotes) {
        return TimeVoteInfoWithStatusResponse.builder()
                .meetStatus(meetStatus)
                .timeInfos(timeVotes.stream()
                        .map(timeVote -> VoteTimeInfo.builder()
                                .timeId(timeVote.getId())
                                .voteTime(timeVote.getTime())
                                .build())
                        .toList())
                .build();
    }
}
