package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.impl.PlaceSlotReader;
import com.prography.yakgwa.domain.vote.impl.PlaceVoteReader;
import com.prography.yakgwa.domain.vote.impl.TimeSlotReader;
import com.prography.yakgwa.domain.vote.impl.TimeVoteReader;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class MeetStatusJudger {
    private final PlaceSlotReader placeSlotReader;
    private final TimeSlotReader timeSlotReader;
    private final PlaceVoteReader placeVoteReader;
    private final TimeVoteReader timeVoteReader;

    public MeetStatus judge(Meet meet, User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validInviteTime = meet.getCreatedDate().plusHours(meet.getValidInviteHour());

        if (validInviteTime.isBefore(now)) {
            return MeetStatus.CONFIRM;
        }

        List<TimeSlot> timeSlots = timeSlotReader.readByMeetId(meet.getId());
        List<PlaceSlot> placeSlots = placeSlotReader.readByMeetId(meet.getId());

        boolean isTimeConfirm = timeSlots.stream().anyMatch(TimeSlot::isConfirm);
        boolean isPlaceConfirm = placeSlots.stream().anyMatch(PlaceSlot::isConfirm);

        if (isTimeConfirm && isPlaceConfirm) {
            return MeetStatus.CONFIRM;
        }

        boolean isUserVotePlace = placeVoteReader.existsByUserId(user.getId());
        boolean isUserVoteTime = timeVoteReader.existsByUserId(user.getId());

        if (!isUserVotePlace && !isUserVoteTime) {
            return MeetStatus.BEFORE_VOTE;
        }

        return MeetStatus.VOTE;
    }
}
