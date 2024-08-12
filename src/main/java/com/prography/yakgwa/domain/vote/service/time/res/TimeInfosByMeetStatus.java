package com.prography.yakgwa.domain.vote.service.time.res;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TimeInfosByMeetStatus {
    private VoteStatus voteStatus;
    private List<TimeSlot> timeSlots;
    private Meet meet;

    public static TimeInfosByMeetStatus of(VoteStatus status,List<TimeSlot>timeSlots,Meet meet){
        return TimeInfosByMeetStatus.builder()
                .meet(meet)
                .voteStatus(status)
                .timeSlots(timeSlots)
                .build();
    }
}
