package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ImplService
@RequiredArgsConstructor
public class PlaceVoteReader {
    private final PlaceVoteJpaRepository placeVoteJpaRepository;

    public boolean existsByUserIdAndMeetId(Long userId,Long meetId) {
        return placeVoteJpaRepository.existsByUserIdAndMeetId(userId,meetId);
    }

    public List<PlaceVote>findAllPlaceVoteOfUserInMeet(Long userId,Long meetId){
        return placeVoteJpaRepository.findAllByUserIdAndMeetId(userId, meetId);
    }

    public List<PlaceVote>findAllInMeet(Long meetId){
        return placeVoteJpaRepository.findAllInMeet(meetId);
    }
    public List<PlaceVote>readAllByPlaceSlotIdWithUser(Long slotId){
        return placeVoteJpaRepository.findAllByPlaceSlotId(slotId);
    }
}
