package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.vote.impl.VoteManager;
import com.prography.yakgwa.domain.vote.impl.dto.VoteInfoWithStatus;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class MeetManager {
    private final VoteManager voteManager;

    public MeetWithVoteAndStatus createMeetWithVoteAndStatus(Participant participant, Long userId) {
        Meet meet = participant.getMeet();
        VoteInfoWithStatus voteInfoWithStatus = voteManager.getVoteInfoWithStatus(userId, meet);
        return MeetWithVoteAndStatus.builder()
                .meet(meet)
                .meetStatus(voteInfoWithStatus.getMeetStatus())
                .placeVote(voteInfoWithStatus.getConfirmPlace())
                .timeVote(voteInfoWithStatus.getConfirmTime())
                .build();
    }
}
