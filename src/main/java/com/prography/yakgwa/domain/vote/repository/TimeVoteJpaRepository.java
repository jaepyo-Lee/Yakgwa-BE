package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.vote.entity.TimeVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeVoteJpaRepository extends JpaRepository<TimeVote,Long> {
    List<TimeVote> findAllByUserId(Long userId);

    List<TimeVote> findAllByMeetId(Long meetId);
}
