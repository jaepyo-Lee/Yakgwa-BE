package com.prography.yakgwa.domain.vote.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Schema(description = "투표한 장소의 아이디값<br>" +
        "이전에 투표했던 값들이더라도 전부 보내주세요!<br>" +
        "투표 취소한 값은 안보내주셔도 됩니다!")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotePlaceRequest {
    @Schema(description = "투표한 장소값 리스트")
    @NotNull(message = "투표하기위한 장소를 보내주세요.")
    private Set<Long> currentVotePlaceSlotIds;
}
