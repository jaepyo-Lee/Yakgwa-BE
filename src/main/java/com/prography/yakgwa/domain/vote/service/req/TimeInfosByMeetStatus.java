package com.prography.yakgwa.domain.vote.service.req;

import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.TimeVote;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TimeInfosByMeetStatus {
    private MeetStatus meetStatus;
    private List<TimeVote> timeVote;
}
