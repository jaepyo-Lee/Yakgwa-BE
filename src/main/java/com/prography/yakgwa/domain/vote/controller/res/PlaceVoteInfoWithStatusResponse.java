package com.prography.yakgwa.domain.vote.controller.res;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "사용자가 투표한 장소목록<br>" +
        "모임의 상태에 따라 placeInfos의 경우 안나올수 있음<br>" +
        "meetStatus : CONFIRM , placeInfo : 확정된 장소의 정보<br>" +
        "meetStatus : BEFORE_CONFIRM(약과장에게만 나감) , placeInfo : 확정해야하는 후보지<br>" +
        "meetStatus : VOTE , placeInfo : 사용자의 투표정보<br>" +
        "meetStatus : BEFORE_VOTE , placeInfo : 아무값 안나감")
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceVoteInfoWithStatusResponse {
    @Schema(description = "투표 상태")
    private VoteStatus meetStatus;
    @Schema(description = "상황에 따른 장소 정보")
    private List<VotePlaceInfo> placeInfos;


    @Getter
    @Builder
    @Schema(name = "PlaceVoteInfoWithStatusResponse-VotePlaceInfo")
    private static class VotePlaceInfo {
        private Long placeSlotId;
        private String title;
        private String roadAddress;
        private String mapx;
        private String mapy;
    }

    public static PlaceVoteInfoWithStatusResponse of(VoteStatus voteStatus, List<PlaceSlot> places) {

        return PlaceVoteInfoWithStatusResponse.builder()
                .meetStatus(voteStatus)
                .placeInfos(places.isEmpty() ? null : places.stream()
                        .map(placeSlot -> VotePlaceInfo.builder()
                                .placeSlotId(placeSlot.getId())
                                .roadAddress(placeSlot.getPlace().getRoadAddress())
                                .title(placeSlot.getPlace().getTitle())
                                .mapy(placeSlot.getPlace().getMapy())
                                .mapx(placeSlot.getPlace().getMapx())
                                .build()
                        ).toList()
                ).build();
    }
}
