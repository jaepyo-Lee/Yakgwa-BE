package com.prography.yakgwa.domain.vote.controller.res;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceSlotOfMeet {
    @Schema(description = "장소 후보지 id")
    private Long placeSlotId;
    @Schema(description = "장소명")
    private String placeName;
    @Schema(description = "장소 주소")
    private String placeAddress;
    @Schema(description = "투표자정보")
    private List<UserInfo> userInfos;

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "PlaceSlotOfMeet-UserInfo")
    public static class UserInfo {
        @Schema(description = "사용자이름")
        private String username;
        @Schema(description = "사용자 이미지")
        private String imageUrl;
    }

    public static PlaceSlotOfMeet of(PlaceSlot placeSlot, List<User> users) {
        Place place = placeSlot.getPlace();
        return PlaceSlotOfMeet.builder()
                .placeSlotId(placeSlot.getId())
                .placeName(place.getTitle())
                .placeAddress(place.getAddress())
                .userInfos(users.stream().map(user -> UserInfo.builder()
                                .username(user.getName()).imageUrl(user.getImageUrl())
                                .build())
                        .toList())
                .build();
    }
}
