package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
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

    public VoteInfoWithStatus getVoteInfoWithStatus(Long userId, Meet meet) {
        List<PlaceVote> placeVotes = placeVoteReader.readByMeetId(meet.getId());
        List<TimeVote> timeVotes = timeVoteReader.readAllByMeetId(meet.getId());

        Optional<TimeVote> timeVoteConfirm = findConfirmedTimeVote(timeVotes);
        Optional<PlaceVote> placeVoteConfirm = findConfirmedPlaceVote(placeVotes);

        Optional<PlaceVote> userPlaceVote = findUserPlaceVote(placeVotes, userId);
        Optional<TimeVote> userTimeVote = findUserTimeVote(timeVotes, userId);

        MeetStatus meetStatus = meetStatusJudge.judge(meet, timeVoteConfirm, placeVoteConfirm, userPlaceVote, userTimeVote);

        return VoteInfoWithStatus.builder()
                .confirmPlace(meetStatus == MeetStatus.CONFIRM ? placeVoteConfirm.orElse(null) : null)
                .confirmTime(meetStatus == MeetStatus.CONFIRM ? timeVoteConfirm.orElse(null) : null)
                .meetStatus(meetStatus)
                .build();
    }

    private Optional<TimeVote> findConfirmedTimeVote(List<TimeVote> timeVotes) {
        return timeVotes.stream()
                .filter(timeVote -> Boolean.TRUE.equals(timeVote.getConfirm()))
                .findFirst();
    }

    private Optional<PlaceVote> findConfirmedPlaceVote(List<PlaceVote> placeVotes) {
        return placeVotes.stream()
                .filter(placeVote -> Boolean.TRUE.equals(placeVote.getConfirm()))
                .findFirst();
    }

    private Optional<PlaceVote> findUserPlaceVote(List<PlaceVote> placeVotes, Long userId) {
        return placeVotes.stream()
                .filter(placeVote -> placeVote.getUser().getId().equals(userId))
                .findFirst();
    }

    private Optional<TimeVote> findUserTimeVote(List<TimeVote> timeVotes, Long userId) {
        return timeVotes.stream()
                .filter(timeVote -> timeVote.getUser().getId().equals(userId))
                .findFirst();
    }
}
