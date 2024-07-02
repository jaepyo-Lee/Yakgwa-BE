package com.prography.yakgwa.domain.vote.impl.dto;

import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VoteInfoWithStatus {
    private MeetStatus meetStatus;
    private PlaceVote confirmPlace;
    private TimeVote confirmTime;
}
