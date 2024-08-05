package com.prography.yakgwa.domain.meet.service.req;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MeetWithVoteAndStatus {
    private Meet meet;
    private MeetStatus meetStatus;
    private PlaceSlot placeSlot;
    private TimeSlot timeSlot;

    public static MeetWithVoteAndStatus of(Meet meet, TimeSlot timeSlot, PlaceSlot placeSlot, MeetStatus meetStatus) {
        return MeetWithVoteAndStatus.builder()
                .meet(meet)
                .timeSlot(timeSlot)
                .placeSlot(placeSlot)
                .meetStatus(meetStatus).build();
    }
}
