package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.TimeVote;
import com.prography.yakgwa.domain.vote.impl.dto.VoteInfoWithStatus;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class VoteManager {
    private final TimeVoteReader timeVoteReader;
    private final PlaceVoteReader placeVoteReader;
    private final MeetStatusJudger meetStatusJudge;
    private final VoteConfirmFinder confirmFinder;

    public VoteInfoWithStatus getVoteInfoWithStatus(Long userId, Meet meet) {
        List<PlaceVote> placeVotes = placeVoteReader.readByMeetId(meet.getId());
        List<TimeVote> timeVotes = timeVoteReader.readAllByMeetId(meet.getId());

        Optional<TimeVote> timeVoteConfirm = confirmFinder.findConfirmedTimeVote(timeVotes);
        Optional<PlaceVote> placeVoteConfirm = confirmFinder.findConfirmedPlaceVote(placeVotes);

        MeetStatus meetStatus = meetStatusJudge.judge(meet, userId);

        return VoteInfoWithStatus.builder()
                .confirmPlace(meetStatus == MeetStatus.CONFIRM ? placeVoteConfirm.orElse(null) : null)
                .confirmTime(meetStatus == MeetStatus.CONFIRM ? timeVoteConfirm.orElse(null) : null)
                .meetStatus(meetStatus)
                .build();
    }


}
