package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.meet.service.res.MeetInfoWithParticipant;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.param.DataIntegrateException;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetService {
    public static final int CORRECT_CONFIRM_PLACESLOT_SIZE = 1;
    public static final int CORRECT_CONFIRM_TIMESLOT_SIZE = 1;

    private final MeetStatusJudger meetStatusJudger;
    private final MeetJpaRepository meetJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;


    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 1:54
     * Finish-Date) 2024-07-29
     */
    public MeetInfoWithParticipant findWithParticipant(Long meetId) {
        Meet meet = meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
        List<Participant> participants = participantJpaRepository.findAllByMeetId(meetId);
        return MeetInfoWithParticipant.of(meet, participants);
    }

    public List<MeetWithVoteAndStatus> findPostConfirm(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);
        return getAfterVoteMeetOf(user);
    }

    private List<MeetWithVoteAndStatus> getAfterVoteMeetOf(User user) {
        List<Participant> participants = participantJpaRepository.findAllByUserId(user.getId());
        List<MeetWithVoteAndStatus> meetsForUser = new ArrayList<>();
        for (Participant participant : participants) {
            Meet meet = participant.getMeet();
            MeetStatus meetStatus = meetStatusJudger.judgeStatusOf(meet, user);
            if (meetStatus.isAfterVoteStatus()) {
                PlaceSlot placeSlot = getConfirmPlaceSlot(meet);
                TimeSlot timeSlot = getConfirmTimeSlot(meet);
                meetsForUser.add(MeetWithVoteAndStatus.of(meet, timeSlot, placeSlot, meetStatus));
            }
        }
        return meetsForUser;
    }

    private MeetWithVoteAndStatus createMeetWithVoteAndStatus(Participant participant, User user) {
        Meet meet = participant.getMeet();
        MeetStatus meetStatus = meetStatusJudger.judgeStatusOf(meet, user);
        PlaceSlot placeSlot = getConfirmPlaceSlot(meet);
        TimeSlot timeSlot = getConfirmTimeSlot(meet);
        return MeetWithVoteAndStatus.of(meet, timeSlot, placeSlot, meetStatus);
    }

    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 1:54
     * Finish-Date) 2024-07-29
     */
    public List<MeetWithVoteAndStatus> findWithStatus(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);
        List<Participant> participants = readAllParticipantOfUser(userId);
        return getCurrentMeetAndSlotWithStatusList(participants, user);
    }

    private List<MeetWithVoteAndStatus> getCurrentMeetAndSlotWithStatusList(List<Participant> participants, User user) {
        List<MeetWithVoteAndStatus> list = new ArrayList<>();
        for (Participant participant : participants) {
            MeetWithVoteAndStatus meetWithVoteAndStatus = createMeetWithVoteAndStatus(participant, user);
            if (isAlreadyPassMeet(meetWithVoteAndStatus)) {
                continue;
            }
            list.add(meetWithVoteAndStatus);
        }
        return list;
    }


    private boolean isAlreadyPassMeet(MeetWithVoteAndStatus meetWithVoteAndStatus) {
        LocalDateTime now = LocalDateTime.now();
        return (meetWithVoteAndStatus.getMeetStatus().equals(MeetStatus.CONFIRM) && meetWithVoteAndStatus.getTimeSlot().getTime().plusHours(3L).isBefore(now)) ||
                (meetWithVoteAndStatus.getMeetStatus().equals(MeetStatus.BEFORE_CONFIRM) && meetWithVoteAndStatus.getMeet().isConfirmTimeEnd());
    }

    private TimeSlot getConfirmTimeSlot(Meet meet) {
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllConfirmByMeetId(meet.getId());
        if (timeSlots.size() > CORRECT_CONFIRM_TIMESLOT_SIZE) {
            throw new DataIntegrateException();
        }
        return timeSlots.stream().findFirst().orElse(null);
    }

    private PlaceSlot getConfirmPlaceSlot(Meet meet) {
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findConfirmByMeetId(meet.getId());
        if (placeSlots.size() > CORRECT_CONFIRM_PLACESLOT_SIZE) {
            throw new DataIntegrateException();
        }
        return placeSlots.stream().findFirst().orElse(null);
    }

    private List<Participant> readAllParticipantOfUser(Long userId) {
        return participantJpaRepository.findAllByUserId(userId);
    }


}
