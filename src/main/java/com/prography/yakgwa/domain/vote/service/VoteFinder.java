package com.prography.yakgwa.domain.vote.service;

public interface VoteFinder<T> {
    T findVoteInfoWithStatusOf(Long userId, Long meetId);
}