package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@ImplService
public class MeetStatusJudger {
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final TimeVoteJpaRepository timeVoteJpaRepository;
    private final MeetConfirmChecker meetConfirmChecker;

    @Autowired
    public MeetStatusJudger(PlaceVoteJpaRepository placeVoteJpaRepository,
                            TimeVoteJpaRepository timeVoteJpaRepository,
                            MeetConfirmChecker meetConfirmChecker) {
        this.placeVoteJpaRepository = placeVoteJpaRepository;
        this.timeVoteJpaRepository = timeVoteJpaRepository;
        this.meetConfirmChecker = meetConfirmChecker;
    }

    /**
     * Work) 테스트 코드
     * Write-Date) 2024-07-13
     * Finish-Date) 2024-07-13
     */

    public MeetStatus judgeStatusOf(Meet meet, User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validInviteTime = meet.getVoteTime();

        if (validInviteTime.isBefore(now) && !isMeetConfirm(meet)) { //이미 투표가능시간이 지난시점 Before_Confirm, 만약 최다가 있다면 자동으로 Confirm
            return handleExpiredVoteTime(meet);
        } else if (isMeetConfirm(meet)) { //확정되었으면 Confirm
            return MeetStatus.CONFIRM;
        } else {
            return handleBeforeVote(meet, user);
        }
    }

    private MeetStatus handleExpiredVoteTime(Meet meet) {
        if (!isMeetConfirm(meet)) {
            return MeetStatus.BEFORE_CONFIRM;
        }
        return MeetStatus.CONFIRM;
    }


    private MeetStatus handleBeforeVote(Meet meet, User user) {
        boolean isVotePlace = placeVoteJpaRepository.existsByUserIdAndMeetId(user.getId(), meet.getId());
        boolean isVoteTime = timeVoteJpaRepository.existsByUserIdInMeet(user.getId(), meet.getId());

        if (!isVotePlace && !isVoteTime) {
            return MeetStatus.BEFORE_VOTE;
        }

        return MeetStatus.VOTE;
    }

    private boolean isMeetConfirm(Meet meet) {
        return meetConfirmChecker.isMeetConfirm(meet);
    }
}
