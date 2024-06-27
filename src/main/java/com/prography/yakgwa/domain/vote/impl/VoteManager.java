package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.TimeVote;
import com.prography.yakgwa.domain.vote.impl.dto.VoteInfoWithStatus;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class VoteManager {
    private final TimeVoteReader timeVoteReader;
    private final PlaceVoteReader placeVoteReader;
    private final MeetStatusJudger meetStatusJudge;

    public VoteInfoWithStatus getVoteInfoWithStatus(Long userId, Meet meet) {

        List<PlaceVote> placeVotes = placeVoteReader.readByMeetId(meet.getId());
        List<TimeVote> timeVotes = timeVoteReader.readAllByMeetId(meet.getId());

        Optional<TimeVote> timeVoteConfirm = timeVotes.stream()
                .filter(timeVote -> Boolean.TRUE.equals(timeVote.getConfirm()))
                .findFirst(); // 시간 확정 여부
        Optional<PlaceVote> placeVoteConfirm = placeVotes.stream()
                .filter(placeVote -> Boolean.TRUE.equals(placeVote.getConfirm()))
                .findFirst(); // 장소 확정 여부

        Optional<PlaceVote> userPlaceVote = placeVotes.stream()
                .filter(placeVote -> placeVote.getUser().getId().equals(userId))
                .findFirst(); // 장소 투표 여부
        Optional<TimeVote> userTimeVote = timeVotes.stream()
                .filter(timeVote -> timeVote.getUser().getId().equals(userId))
                .findFirst(); // 시간 투표 여부
        MeetStatus meetStatus = meetStatusJudge.judge(meet, timeVoteConfirm, placeVoteConfirm, userPlaceVote, userTimeVote);
        return VoteInfoWithStatus.builder()
                .confirmPlace(meetStatus.equals(MeetStatus.CONFIRM) ? placeVoteConfirm.get() : null)
                .confirmTime(meetStatus.equals(MeetStatus.CONFIRM) ? timeVoteConfirm.get() : null)
                .meetStatus(meetStatus)
                .build();
    }
}
