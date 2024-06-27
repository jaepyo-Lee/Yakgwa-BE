package com.prography.yakgwa.domain.meet.service.res;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.participant.entity.Participant;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MeetInfoWithParticipant {
    private Meet meet;
    private List<Participant> participants;
    public static MeetInfoWithParticipant of(Meet meet,List<Participant>participants){
        return MeetInfoWithParticipant.builder()
                .meet(meet)
                .participants(participants)
                .build();
    }
}
