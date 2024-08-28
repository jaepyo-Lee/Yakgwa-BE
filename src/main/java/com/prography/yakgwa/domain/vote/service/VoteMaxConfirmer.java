package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class VoteMaxConfirmer {
    private final VoteConfirm timeVoteConfirm;
    private final VoteConfirm placeVoteConfirm;
    private final MeetJpaRepository meetJpaRepository;

    @Autowired
    public VoteMaxConfirmer(TimeConfirm timeVoteConfirm, PlaceConfirm placeVoteConfirm, MeetJpaRepository meetJpaRepository) {
        this.timeVoteConfirm = timeVoteConfirm;
        this.placeVoteConfirm = placeVoteConfirm;
        this.meetJpaRepository = meetJpaRepository;
    }

    public Optional<Meet> confirmMaxVote(Long meetId) {
        Meet meet = meetJpaRepository.findById(meetId).orElseThrow(NotFoundMeetException::new);
        boolean isTimeConfirm = timeVoteConfirm.confirmMaxOf(meet);
        boolean isPlaceConfirm = placeVoteConfirm.confirmMaxOf(meet);
        return isPlaceConfirm && isTimeConfirm ? Optional.of(meet) : Optional.empty();
    }
}
