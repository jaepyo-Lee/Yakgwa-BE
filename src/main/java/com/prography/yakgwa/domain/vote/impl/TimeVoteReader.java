package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@ImplService
public class TimeVoteReader {
    private final TimeVoteJpaRepository timeVoteJpaRepository;

    /**
     * Work) 테스트코드
     * Write-Date) 2024-07-12
     * Finish-Date) 2024-07-12
     */
    public boolean existsByUserIdInMeet(Long userId, Long meetId) {
        return timeVoteJpaRepository.existsByUserIdInMeet(userId,meetId);
    }

    /**
     * Work) 테스트코드 작성
     * Write-Date) 2024-07-12
     * Finish-Date)
     */
    public List<TimeVote> findAllTimeVoteOfUserInMeet(Long userId, Long meetId){
        return timeVoteJpaRepository.findAllByTimeSlotOfUser(userId,meetId);
    }

    public List<TimeVote> readAllInMeet(Long meetId){
        return timeVoteJpaRepository.findAllByMeetId(meetId);
    }
}
