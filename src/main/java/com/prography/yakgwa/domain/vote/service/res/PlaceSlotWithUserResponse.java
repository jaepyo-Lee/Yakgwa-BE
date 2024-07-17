package com.prography.yakgwa.domain.vote.service.res;

import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlaceSlotWithUserResponse {
    private PlaceSlot placeSlot;
    private List<User> users;
}
