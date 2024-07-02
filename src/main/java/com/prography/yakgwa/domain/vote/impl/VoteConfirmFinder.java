package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.TimeVote;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class VoteConfirmFinder {

    public Optional<TimeVote> findConfirmedTimeVote(List<TimeVote> timeVotes) {
        return timeVotes.stream()
                .filter(timeVote -> Boolean.TRUE.equals(timeVote.getConfirm()))
                .findFirst();
    }

    public Optional<PlaceVote> findConfirmedPlaceVote(List<PlaceVote> placeVotes) {
        return placeVotes.stream()
                .filter(placeVote -> Boolean.TRUE.equals(placeVote.getConfirm()))
                .findFirst();
    }
}
