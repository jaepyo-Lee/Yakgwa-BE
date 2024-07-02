package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@ImplService
public class TimeVoteWriter {
    private final TimeVoteJpaRepository timeVoteJpaRepository;
    private final TimeSlotWriter timeSlotWriter;

    public TimeVote write(User user, TimeSlot timeSlot) {
        TimeVote timeVote = TimeVote.builder()
                .timeSlot(timeSlot)
                .user(user)
                .build();
        return timeVoteJpaRepository.save(timeVote);
    }

    public void confirmAndWrite(Meet meet, LocalDateTime meetTime) {
        if (meetTime == null) { //해당 값이 있다면 투표,null이면 투표
            return;
        }
        TimeSlot timeSlot = TimeSlot.builder().meet(meet).time(meetTime).confirm(Boolean.TRUE).build();
        timeSlotWriter.write(timeSlot);
    }
}
