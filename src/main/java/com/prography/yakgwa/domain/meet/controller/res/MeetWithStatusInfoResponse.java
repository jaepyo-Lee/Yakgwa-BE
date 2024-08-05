package com.prography.yakgwa.domain.meet.controller.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
        @Schema(description = "모임 상태")
        private MeetStatus meetStatus;
        @Schema(description = "모임정보<br>" +
                "meetStatus가 Confirm일때만 해당 정보 반환됌. null값 보내주는게 편하시면 말씀해주세요!")
        private MeetInfo meetInfo;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Schema(name = "MeetWithStatusInfoResponse-meetInfo")
    private static class MeetInfo {
        @Schema(description = "모임 테마명",example = "데이트")
        private String meetThemeName;
        @Schema(description = "모임일자",example = "2024-07-10")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime meetDateTime;
        @Schema(description = "모임 장소",example = "스타벅스")
        private String placeName;
        @Schema(description = "모임명",example = "다음 세션 모임")
        private String meetTitle;
        @Schema(description = "모임id",example = "1")
        private Long meetId;
        @Schema(description = "모임설명", example = "설명")
        private String description;
    }

    public static MeetWithStatusInfoResponse of(List<MeetWithVoteAndStatus> list) {
        return MeetWithStatusInfoResponse.builder().meetInfosWithStatus(
                list.stream().map(meetWithVoteAndStatus -> {
                    Meet meet = meetWithVoteAndStatus.getMeet();
                    return MeetInfoWithStatus.builder()
                            .meetStatus(meetWithVoteAndStatus.getMeetStatus())
                            .meetInfo(MeetInfo.builder()
                                    .meetDateTime(meetWithVoteAndStatus.getTimeSlot() == null ? null : meetWithVoteAndStatus.getTimeSlot().getTime())
                                    .meetId(meet.getId())
                                    .meetThemeName(meet.getMeetTheme().getName())
                                    .placeName(meetWithVoteAndStatus.getPlaceSlot() == null ? null : meetWithVoteAndStatus.getPlaceSlot().getPlace().getTitle())
                                    .meetTitle(meet.getTitle())
                                    .description(meet.getDescription())
                                    .build())
                            .build();
                }).toList()
        ).build();
    }
}
