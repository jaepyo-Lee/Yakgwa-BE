package com.prography.yakgwa.domain.vote.controller.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.service.dto.VoteDateDto;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "사용자가 투표한 시간목록<br>" +
        "meetStatus : CONFIRM , timeInfos : 확정된 시간 정보 , voteDate : 안나옴 <br>" +
        "meetStatus : BEFOR_CONFIRM , timeInfos : 확정지어줘야하는 시간목록 , voteDate : 안나옴 <br>" +
        "meetStatus : VOTE , timeInfos : 사용자의 투표정보 , voteDate : 투표가능한 시간 <br>" +
        "meetStatus : BEFORE_VOTE , timeInfos : 빈 리스트 , voteDate : 투표가능한 시간 ")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class TimeVoteInfoWithStatusResponse {
    @Schema(description = "투표 상태")
    private VoteStatus meetStatus;
    @Schema(description = "상황에 따른 시간 정보")
    private List<VoteTimeInfo> timeInfos;
    @Schema(description = "모임 투표가능한 시간")
    private VoteDateDto voteDate;

    @Getter
    @Builder
    @Schema(name = "TimeVoteInfoWithStatusResponse-VoteTimeInfo")
    private static class VoteTimeInfo {
        private Long timeId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        @Schema(example = "2024-07-16 20:00")
        private LocalDateTime voteTime;
    }

    public static TimeVoteInfoWithStatusResponse of(VoteStatus voteStatus, List<TimeSlot> timeSlots, Meet meet) {
        VoteDateDto voteDateDto = null;
        if (voteStatus == VoteStatus.BEFORE_VOTE || voteStatus == VoteStatus.VOTE) {
            voteDateDto = VoteDateDto.builder()
                    .startVoteDate(meet.getPeriod().getStartDate())
                    .endVoteDate(meet.getPeriod().getEndDate())
                    .build();
        }
        return TimeVoteInfoWithStatusResponse.builder()
                .meetStatus(voteStatus)
                .timeInfos(timeSlots.isEmpty() ? null : timeSlots.stream()
                        .map(timeVote -> VoteTimeInfo.builder()
                                .timeId(timeVote.getId())
                                .voteTime(timeVote.getTime())
                                .build())
                        .toList())
                .voteDate(voteDateDto)
                .build();
    }
}
