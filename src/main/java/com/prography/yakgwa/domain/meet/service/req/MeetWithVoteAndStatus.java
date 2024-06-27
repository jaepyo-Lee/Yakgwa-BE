package com.prography.yakgwa.domain.meet.service.req;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.TimeVote;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MeetWithVoteAndStatus {
    private Meet meet;
    private MeetStatus meetStatus;
    private PlaceVote placeVote;
    private TimeVote timeVote;
}
