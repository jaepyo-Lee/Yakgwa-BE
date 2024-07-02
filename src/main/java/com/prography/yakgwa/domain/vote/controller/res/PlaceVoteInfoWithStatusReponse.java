package com.prography.yakgwa.domain.vote.controller.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.place.entity.Place;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceVoteInfoWithStatusReponse {
    private MeetStatus meetStatus;
    private List<VotePlaceInfo> placeInfos;

    @Getter
    @Builder
    private static class VotePlaceInfo {
        private Long placeId;
        private String title;
        private String roadAddress;
        private String mapx;
        private String mapy;
    }

    public static PlaceVoteInfoWithStatusReponse of(MeetStatus meetStatus, List<Place> places) {
        return PlaceVoteInfoWithStatusReponse.builder()
                .meetStatus(meetStatus)
                .placeInfos(places.isEmpty() ? null : places.stream()
                        .map(place -> VotePlaceInfo.builder()
                                .placeId(place.getId())
                                .mapx(place.getMapx())
                                .mapy(place.getMapy())
                                .roadAddress(place.getRoadAddress())
                                .title(place.getTitle()).build())
                        .toList())
                .build();
    }
}
