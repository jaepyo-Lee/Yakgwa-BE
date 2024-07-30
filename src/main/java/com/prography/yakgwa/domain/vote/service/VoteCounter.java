package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ImplService
public class VoteCounter {
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final TimeVoteJpaRepository timeVoteJpaRepository;
    /**
     * Todo
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 15:40
     * Finish-Date)
     */
    public List<PlaceSlot> findMaxVotePlaceSlotFrom(Meet meet) {
        List<PlaceVote> allInMeet = placeVoteJpaRepository.findAllInMeet(meet.getId());

        Map<PlaceSlot, Long> placeSlotVoteCounts = allInMeet.stream()
                .collect(Collectors.groupingBy(PlaceVote::getPlaceSlot, Collectors.counting()));

        long maxVoteCount = placeSlotVoteCounts.values().stream()
                .max(Long::compare)
                .orElse(0L);

        return placeSlotVoteCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVoteCount)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Todo
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 15:47
     * Finish-Date)
     */
    public List<TimeSlot> findMaxVoteTimeSlotFrom(Meet meet) {
        List<TimeVote> timeVotes = timeVoteJpaRepository.findAllByMeetId(meet.getId());

        Map<TimeSlot, Long> timeSlotVoteCounts = timeVotes.stream()
                .collect(Collectors.groupingBy(TimeVote::getTimeSlot, Collectors.counting()));

        long maxVoteCount = timeSlotVoteCounts.values().stream()
                .max(Long::compare)
                .orElse(0L);

        return timeSlotVoteCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVoteCount)
                .map(Map.Entry::getKey)
                .toList();
    }
}
