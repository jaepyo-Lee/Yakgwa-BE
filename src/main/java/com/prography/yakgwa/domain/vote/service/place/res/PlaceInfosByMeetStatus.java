package com.prography.yakgwa.domain.vote.service.place.res;

import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlaceInfosByMeetStatus {
    private VoteStatus voteStatus;
    private List<PlaceSlot> places;

    public static PlaceInfosByMeetStatus of(VoteStatus voteStatus, List<PlaceSlot> places) {
        return PlaceInfosByMeetStatus.builder()
                .places(places)
                .voteStatus(voteStatus)
                .build();
    }
}
