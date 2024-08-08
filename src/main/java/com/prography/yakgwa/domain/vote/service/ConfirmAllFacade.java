package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmAllFacade {
    private final MeetJpaRepository meetJpaRepository;
    private final VoteConfirm placeConfirm;
    private final VoteConfirm timeConfirm;

    @Autowired
    public ConfirmAllFacade(MeetJpaRepository meetJpaRepository, PlaceConfirm placeConfirm, TimeConfirm timeConfirm) {
        this.meetJpaRepository = meetJpaRepository;
        this.placeConfirm = placeConfirm;
        this.timeConfirm = timeConfirm;
    }

    public void calConfirm(Long meetId) {
        Meet meet = meetJpaRepository.findById(meetId).orElseThrow(NotFoundMeetException::new);
        placeConfirm.confirmMaxOf(meet);
        timeConfirm.confirmMaxOf(meet);
    }
}
