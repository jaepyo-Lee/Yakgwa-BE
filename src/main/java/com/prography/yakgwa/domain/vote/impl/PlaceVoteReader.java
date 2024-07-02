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

    public boolean existsByUserId(Long userId) {
        return placeVoteJpaRepository.existsByUserId(userId);
    }

    public List<PlaceVote>findAllPlaceVoteOfUserInMeet(Long userId,List<Long>placeVoteIds){
        return placeVoteJpaRepository.findAllByPlaceSlotOfUser(userId, placeVoteIds);
    }
}
