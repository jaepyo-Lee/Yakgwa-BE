package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ImplService
@RequiredArgsConstructor
public class PlaceVoteReader {
    private final PlaceVoteJpaRepository placeVoteJpaRepository;

    public List<PlaceVote> readByUserId(Long userId) {
        return placeVoteJpaRepository.findAllByUserId(userId);
    }

    public List<PlaceVote> readByMeetId(Long meetId) {
        return placeVoteJpaRepository.findAllByMeetId(meetId);
    }

    public List<PlaceVote> readByUserIdAndMeetId(Long userId, Long meetId) {
        return placeVoteJpaRepository.findAllByUserIdAndMeetId(userId, meetId);
    }

}
