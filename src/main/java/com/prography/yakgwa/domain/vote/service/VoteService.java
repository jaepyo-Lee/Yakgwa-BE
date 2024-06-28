package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.TimeVote;
import com.prography.yakgwa.domain.vote.impl.PlaceVoteReader;
import com.prography.yakgwa.domain.vote.impl.TimeVoteReader;
import com.prography.yakgwa.domain.vote.impl.VoteConfirmFinder;
import com.prography.yakgwa.domain.vote.service.req.PlaceInfosByMeetStatus;
import com.prography.yakgwa.domain.vote.service.req.TimeInfosByMeetStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final MeetReader meetReader;
    private final PlaceVoteReader placeVoteReader;
    private final TimeVoteReader timeVoteReader;
    private final VoteConfirmFinder voteConfirmFinder;

    public PlaceInfosByMeetStatus findPlaceInfoWithMeetStatus(Long userId, Long meetId) {
        Meet meet = meetReader.read(meetId);
        List<PlaceVote> placeVotes = placeVoteReader.readByMeetId(meetId);
        Optional<PlaceVote> confirmedPlaceVote = voteConfirmFinder.findConfirmedPlaceVote(placeVotes);

        if (confirmedPlaceVote.isPresent()) { //장소확정되었을때
            PlaceVote placeVote = confirmedPlaceVote.get();
            return PlaceInfosByMeetStatus.builder()
                    .meetStatus(MeetStatus.CONFIRM)
                    .places(List.of(placeVote.getPlace()))
                    .build();
        } else {
            List<PlaceVote> votes = placeVotes.stream().filter(placeVote -> placeVote.getUser().getId().equals(userId)).toList();
            if (!votes.isEmpty()) { //사용자가 투표했을때
                return PlaceInfosByMeetStatus.builder()
                        .meetStatus(MeetStatus.VOTE)
                        .places(votes.stream().map(PlaceVote::getPlace).toList())
                        .build();
            } else { //사용자가 투표 안했을때
                return PlaceInfosByMeetStatus.builder()
                        .meetStatus(MeetStatus.BEFORE_VOTE)
                        .places(placeVotes.stream().map(PlaceVote::getPlace).toList())
                        .build();
            }
        }
    }

    public TimeInfosByMeetStatus findTimeInfoWithMeetStatus(Long userId, Long meetId) {
        Meet meet = meetReader.read(meetId);
        List<TimeVote> timeVotes = timeVoteReader.readAllByMeetId(meetId);
        Optional<TimeVote> confirmedTimeVote = voteConfirmFinder.findConfirmedTimeVote(timeVotes);

        if (confirmedTimeVote.isPresent()) { // 시간확정되었을때
            TimeVote timeVote = confirmedTimeVote.get();
            return TimeInfosByMeetStatus.builder()
                    .meetStatus(MeetStatus.CONFIRM)
                    .timeVote(List.of(timeVote))
                    .build();
        } else {
            List<TimeVote> votes = timeVotes.stream().filter(timeVote -> timeVote.getUser().getId().equals(userId)).toList();
            if (!votes.isEmpty()) { //사용자가 투표했을때
                return TimeInfosByMeetStatus.builder()
                        .meetStatus(MeetStatus.VOTE)
                        .timeVote(votes)
                        .build();
            } else { //사용자가 투표 안했을때
                return TimeInfosByMeetStatus.builder()
                        .meetStatus(MeetStatus.BEFORE_VOTE)
                        .timeVote(timeVotes)
                        .build();
            }
        }
    }
}
