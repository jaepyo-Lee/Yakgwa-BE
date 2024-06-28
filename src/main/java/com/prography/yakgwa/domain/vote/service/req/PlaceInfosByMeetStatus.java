package com.prography.yakgwa.domain.vote.service.req;

import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlaceInfosByMeetStatus {
    private MeetStatus meetStatus;
    private List<Place> places;

}
