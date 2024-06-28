package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.TimeVote;
import com.prography.yakgwa.domain.vote.impl.PlaceVoteReader;
import com.prography.yakgwa.domain.vote.impl.TimeVoteReader;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class MeetStatusJudger {
    private final TimeVoteReader timeVoteReader;
    private final PlaceVoteReader placeVoteReader;

    public MeetStatus judge(Meet meet, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validInviteTime = meet.getCreatedDate().plusHours(meet.getValidInviteHour());
        List<PlaceVote> placeVotes = placeVoteReader.readByMeetId(meet.getId());
        List<TimeVote> timeVotes = timeVoteReader.readAllByMeetId(meet.getId());

        Optional<TimeVote> timeVoteConfirm = findConfirmedTimeVote(timeVotes);
        Optional<PlaceVote> placeVoteConfirm = findConfirmedPlaceVote(placeVotes);

        Optional<PlaceVote> userPlaceVote = findUserPlaceVote(placeVotes, userId);
        Optional<TimeVote> userTimeVote = findUserTimeVote(timeVotes, userId);

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
