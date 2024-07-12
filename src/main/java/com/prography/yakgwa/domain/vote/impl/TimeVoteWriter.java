package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.impl.dto.ConfirmTimeDto;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@ImplService
public class TimeVoteWriter {
    private final TimeVoteJpaRepository timeVoteJpaRepository;
    private final TimeSlotWriter timeSlotWriter;

    public void confirmAndWrite(Meet meet, ConfirmTimeDto confirmTimeDto) {
        if (confirmTimeDto.getMeetTime() == null) { //해당 값이 있다면 확정,null이면 투표
            return;
        }
        TimeSlot timeSlot = TimeSlot.builder()
                .meet(meet).time(confirmTimeDto.getMeetTime()).confirm(Boolean.TRUE)
                .build();
        timeSlotWriter.write(timeSlot);
    }

    /**
     * Work) 테스트코드 작성
     * Write-Date) 2024-07-11
     * Finish-Date) 2024-07-12
     */
    public void deleteAllVoteOfUser(User user, Long meetId) {
        timeVoteJpaRepository.deleteAllByUserIdAndTimeSlotIdIn(user, meetId);
    }

    /**
     * Work) 테스트코드 작성
     * Write-Date) 2024-07-11
     * Finish-Date) 2024-07-12
     */
    public List<TimeVote> writeAll(User user, List<TimeSlot> chooseTimeSlot) {
        List<TimeVote> timeVotes = chooseTimeSlot.stream().map(timeSlot -> TimeVote.builder()
                .timeSlot(timeSlot)
                .user(user)
                .build()).toList();
        return timeVoteJpaRepository.saveAll(timeVotes);
    }
}
