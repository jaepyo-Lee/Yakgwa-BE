package com.prography.yakgwa.domain.vote.controller.res;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PlaceSlotOfMeet {
    private Long placeSlotId;
    private String placeName;
    private String placeAddress;
    private List<UserInfo> userInfos;

    @Builder
    @Getter
    private static class UserInfo {
        private String username;
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
