package com.prography.yakgwa.domain.vote.service.req;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.place.entity.Place;
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
    private Meet meet;

    public static PlaceInfosByMeetStatus of(VoteStatus voteStatus, List<PlaceSlot> places, Meet meet) {
        return PlaceInfosByMeetStatus.builder()
                .meet(meet).places(places).voteStatus(voteStatus)
                .build();
    }
}
