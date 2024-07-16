package com.prography.yakgwa.domain.vote.service.req;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TimeInfosByMeetStatus {
    private VoteStatus voteStatus;
    private List<TimeSlot> timeSlots;
    private Meet meet;
}
