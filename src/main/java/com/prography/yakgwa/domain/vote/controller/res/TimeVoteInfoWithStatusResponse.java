package com.prography.yakgwa.domain.vote.controller.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "사용자가 투표한 시간목록<br>" +
        "meetStatus : CONFIRM , timeInfos : 확정된 시간 정보<br>" +
        "meetStatus : BEFOR_CONFIRM(약과장에게만 나감) , timeInfos : 확정지어줘야하는 시간목록<br>" +
        "meetStatus : VOTE , timeInfos : 사용자의 투표정보<br>" +
        "meetStatus : BEFORE_VOTE , timeInfos : 빈 리스트")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class TimeVoteInfoWithStatusResponse {
    @Schema(description = "투표 상태")
    private VoteStatus meetStatus;
    @Schema(description = "상황에 따른 시간 정보")
    private List<VoteTimeInfo> timeInfos;

    @Getter
    @Builder
    @Schema(name = "TimeVoteInfoWithStatusResponse-VoteTimeInfo")
    private static class VoteTimeInfo {
        private Long timeId;
        private LocalDateTime voteTime;
    }

    public static TimeVoteInfoWithStatusResponse of(VoteStatus voteStatus, List<TimeSlot> timeSlots) {
        return TimeVoteInfoWithStatusResponse.builder()
                .meetStatus(voteStatus)
                .timeInfos(timeSlots.isEmpty() ? null : timeSlots.stream()
                        .map(timeVote -> VoteTimeInfo.builder()
                                .timeId(timeVote.getId())
                                .voteTime(timeVote.getTime())
                                .build())
                        .toList())
                .build();
    }
}
