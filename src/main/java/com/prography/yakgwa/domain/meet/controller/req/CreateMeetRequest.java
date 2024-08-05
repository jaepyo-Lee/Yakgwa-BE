package com.prography.yakgwa.domain.meet.controller.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prography.yakgwa.domain.common.validator.date.DateValid;
import com.prography.yakgwa.domain.common.validator.datetime.DateTimeValid;
import com.prography.yakgwa.domain.meet.service.dto.VoteDateDto;
import com.prography.yakgwa.domain.meet.service.req.MeetCreateRequestDto;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.global.format.exception.param.TimeParamException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreateMeetRequest {
    @Schema(description = "모임정보")
    @Valid
    @NotNull
    private MeetInfo meetInfo;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Schema(name = "CreateMeetRequest-meetInfo")
    public static class MeetInfo {
        @Schema(description = "모임명", example = "다음 세션 모임")
        @NotBlank(message = "모임명은 필수요소입니다.")
        private String meetTitle;

        @Schema(description = "모임설명", example = "다음세션화이팅!")
        private String description;

        @Schema(description = "모임테마id", example = "1")
        @NotNull
        private Long meetThemeId;

        @Schema(description = "모임 장소 확정 여부", example = "true")
        @NotNull
        private Boolean confirmPlace;

        //null이라면 장소투표로
        @Schema(description = "모임장소정보<br>" +
                "장소 확정시에는 1개만 넣어주세요!")
        @Builder.Default
        @NotNull
        private List<PlaceInfoDto> placeInfo = new ArrayList<>();

        // 해당값이 있다면 투표, null이라면 확정
        @Schema(description = "투표 시간 범위값<br>" +
                "만약 시간을 확정지어졌다면 해당값은 null 넣어주세요.")
        private VoteDate voteDate;

        // 해당값이 있다면 확정, null이면 투표
        @Schema(description = "시간확정값<br>" +
                "시간을 투표로 넘긴다면 해당값은 null 넣어주세요.", pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime meetTime;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Schema(name = "CreateMeetRequest-voteDate")
    public static class VoteDate {
        @Schema(description = "모임 투표 시작시간범위", example = "2024-07-10")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate startVoteDate;

        @Schema(description = "모임 투표 마감시간범위", example = "2024-07-10")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate endVoteDate;
    }

    public MeetCreateRequestDto toRequestDto(Long creatorId) {
        if ((meetInfo.voteDate == null && meetInfo.meetTime == null) ||
                (meetInfo.voteDate != null && meetInfo.meetTime != null)) {
            throw new TimeParamException();
        }
        return MeetCreateRequestDto.builder()
                .creatorId(creatorId)
                .description(meetInfo.description)
                .title(meetInfo.meetTitle)
                .confirmPlace(meetInfo.confirmPlace)
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
