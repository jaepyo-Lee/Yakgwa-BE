package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.TimeVote;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class MeetStatusJudger {
    public MeetStatus judge(Meet meet, Optional<TimeVote> timeVoteConfirm, Optional<PlaceVote> placeVoteConfirm, Optional<PlaceVote> userPlaceVote, Optional<TimeVote> userTimeVote) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validInviteTime = meet.getCreatedDate().plusHours(meet.getValidInviteHour());

        if (validInviteTime.isBefore(now) || (timeVoteConfirm.isPresent() && placeVoteConfirm.isPresent())) {
            return MeetStatus.CONFIRM;
        } else if ((timeVoteConfirm.isPresent() && userPlaceVote.isPresent()) ||
                (userTimeVote.isPresent() && placeVoteConfirm.isPresent()) ||
                (userTimeVote.isPresent() && userPlaceVote.isPresent())) {
            return MeetStatus.VOTE;
        } else {
            return MeetStatus.BEFORE_VOTE;
        }
    }
}
