package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.TimeVote;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@ImplService
public class TimeVoteReader {
    private final TimeVoteJpaRepository timeVoteJpaRepository;

    public List<TimeVote> readAllByUserId(Long userId){
        return timeVoteJpaRepository.findAllByUserId(userId);
    }
    public List<TimeVote> readAllByMeetId(Long meetId){
        return timeVoteJpaRepository.findAllByMeetId(meetId);
    }
}
