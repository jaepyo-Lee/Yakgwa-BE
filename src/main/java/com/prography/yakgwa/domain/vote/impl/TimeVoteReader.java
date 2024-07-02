package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@ImplService
public class TimeVoteReader {
    private final TimeVoteJpaRepository timeVoteJpaRepository;

    public boolean existsByUserId(Long userId) {
        return timeVoteJpaRepository.existsByUserId(userId);
    }

    public boolean existsConfirm(Long meetId) {
        return timeVoteJpaRepository.existsByMeetId(meetId);
    }
    public List<TimeVote> findAllTimeVoteOfUserInMeet(Long userId, List<Long>timeSlotIds){
        return timeVoteJpaRepository.findAllByTimeSlotOfUser(userId, timeSlotIds);
    }
}
