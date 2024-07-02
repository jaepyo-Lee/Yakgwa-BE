package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class PlaceVoteReader {
    private final PlaceVoteJpaRepository placeVoteJpaRepository;

    public boolean existsByUserId(Long userId) {
        return placeVoteJpaRepository.existsByUserId(userId);
    }
}
