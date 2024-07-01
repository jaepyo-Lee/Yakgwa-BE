package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@ImplService
public class TimeVoteWriter {
    private final TimeVoteJpaRepository timeVoteJpaRepository;

    public TimeVote write(User user, Meet meet, LocalDateTime time, Boolean confirm) {
        TimeVote timeVote = TimeVote.builder()
                .time(time)
                .user(user)
                .meet(meet)
                .confirm(confirm)
                .build();
        return timeVoteJpaRepository.save(timeVote);
    }

    public void confirmAndWrite(User user, Meet meet, LocalDateTime meetTime) {
        if (meetTime == null) { //해당 값이 있다면 투표,null이면 투표
            return;
        }
        //해당값이 있다면 확정,null이면 투표
        write(user, meet, meetTime, Boolean.TRUE);
    }
}
