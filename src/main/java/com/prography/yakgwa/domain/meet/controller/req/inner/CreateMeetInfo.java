package com.prography.yakgwa.domain.meet.controller.req.inner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prography.yakgwa.domain.meet.controller.req.CreateMeetRequest;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreateMeetInfo {
    @Schema(description = "모임명", example = "다음 세션 모임")
    @NotBlank(message = "모임명은 필수요소입니다.")
    private String meetTitle;

    @Schema(description = "모임설명", example = "다음세션화이팅!")
    @NotNull
    private String description;

    @Schema(description = "모임테마id", example = "1")
    @NotNull
    private Long meetThemeId;

    @Schema(description = "모임 장소 확정 여부", example = "true")
    @NotNull
    private boolean confirmPlace;

    //null이라면 장소투표로
    @Schema(description = "모임장소정보<br>" +
            "장소 확정시에는 1개만 넣어주세요!")
    @Builder.Default
    @NotNull
    private List<PlaceInfoDto> placeInfo = new ArrayList<>();

    // 해당값이 있다면 투표, null이라면 확정
    @Schema(description = "투표 시간 범위값<br>" +
            "만약 시간을 확정지어졌다면 해당값은 null 넣어주세요.")
    @Valid
    private CreateMeetVoteDate voteDate;

    // 해당값이 있다면 확정, null이면 투표
    @Schema(description = "시간확정값<br>" +
            "시간을 투표로 넘긴다면 해당값은 null 넣어주세요.", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime meetTime;
}
