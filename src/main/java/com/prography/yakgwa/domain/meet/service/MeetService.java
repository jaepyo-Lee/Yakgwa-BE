package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.meet.impl.MeetWriter;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.service.req.MeetCreateRequestDto;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.meet.service.res.MeetInfoWithParticipant;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.impl.ParticipantWriter;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.format.exception.meet.ConfirmPlaceCountException;
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

    private final MeetWriter meetWriter;
    private final ParticipantWriter participantWriter;
    private final MeetStatusJudger meetStatusJudger;
    private final MeetJpaRepository meetJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;

    /**
     * Todo
     * 요청 dto에서 투표확정시간과 투표가능시간을 둘다 받는데 이것을 모임 생성할때
     * 해당 값들을 검증하는게 맞을까? 둘다 null이거나 둘다 값이 들어가있는경우 예외처리해야하는데 현재는 meetWriter.write에서 처리하고 있음
     */
    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 1:54
     * Finish-Date) 2024-07-29
     */
    @Transactional
    public Meet create(MeetCreateRequestDto requestDto) {
        User user = readUser(requestDto.getCreatorId());
        Meet meet = createMeet(requestDto);

        savePlaceSlotOfNewMeet(requestDto, meet);
        saveTimeSlotIfNotConfirm(requestDto, meet);

        participantWriter.registLeader(meet, user);
        return meet;
    }


    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 1:54
     * Finish-Date) 2024-07-29
     */
    public MeetInfoWithParticipant findWithParticipant(Long meetId) {
        Meet meet = readMeet(meetId);
        List<Participant> participants = readAllParticipantOfMeet(meetId);
        return MeetInfoWithParticipant.of(meet, participants);
    }

    /**
     * Todo
     * Work) Test Code, 시간에 대한 처리가 필요해서 추후 테스트하기!
     * Write-Date) 2024-07-29, 월, 1:54
     * Finish-Date)
     */
    public List<MeetWithVoteAndStatus> findPostConfirm(Long userId) {
        List<MeetWithVoteAndStatus> list = findWithStatus(userId).stream()
                .filter(meet -> meet.getMeetStatus().equals(MeetStatus.CONFIRM))
                .filter(meet -> meet.getTimeSlot().getTime().isBefore(LocalDateTime.now().minusHours(1L)))
                .toList();
        return list;
    }

    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 1:54
     * Finish-Date) 2024-07-29
     */
    public List<MeetWithVoteAndStatus> findWithStatus(Long userId) {
        User user = readUser(userId);
        List<Participant> participants = readAllParticipantOfUser(userId);

        return getMeetAndSlotWithStatusList(participants, user);
    }

    private List<MeetWithVoteAndStatus> getMeetAndSlotWithStatusList(List<Participant> participants, User user) {
        List<MeetWithVoteAndStatus> list = new ArrayList<>();
        for (Participant participant : participants) {
            Meet meet = participant.getMeet();
            MeetStatus meetStatus = meetStatusJudger.judge(meet, user);

            PlaceSlot placeSlot = getConfirmPlaceSlot(meet);
            TimeSlot timeSlot = getConfirmTimeSlot(meet);

            list.add(MeetWithVoteAndStatus.of(meet, timeSlot, placeSlot, meetStatus));
        }
        return list;
    }

    private TimeSlot getConfirmTimeSlot(Meet meet) {
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllConfirmByMeetId(meet.getId());
        if (timeSlots.size() > CORRECT_CONFIRM_TIMESLOT_SIZE) {
            throw new DataIntegrateException();
        }
        return timeSlots.stream()
                .findFirst()
                .orElse(null);
    }

    private PlaceSlot getConfirmPlaceSlot(Meet meet) {
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findConfirmByMeetId(meet.getId());
        if (placeSlots.size() > CORRECT_CONFIRM_PLACESLOT_SIZE) {
            throw new DataIntegrateException();
        }
        return placeSlots.stream()
                .findFirst()
                .orElse(null);
    }

    private Meet createMeet(MeetCreateRequestDto requestDto) {
        return meetWriter.write(requestDto.toMeetWriteDto());
    }

    private List<Participant> readAllParticipantOfUser(Long userId) {
        return participantJpaRepository.findAllByUserId(userId);
    }

    private User readUser(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }

    private Meet readMeet(Long meetId) {
        return meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
    }

    private List<Participant> readAllParticipantOfMeet(Long meetId) {
        return participantJpaRepository.findAllByMeetId(meetId);
    }

    private void saveTimeSlotIfNotConfirm(MeetCreateRequestDto requestDto, Meet meet) {
        if (requestDto.getMeetTime() == null) {
            return;
        }
        TimeSlot timeSlot = TimeSlot.builder()
                .meet(meet).time(requestDto.getMeetTime()).confirm(Boolean.TRUE)
                .build();
        timeSlotJpaRepository.save(timeSlot);
    }

    private void savePlaceSlotOfNewMeet(MeetCreateRequestDto requestDto, Meet meet) {
        List<PlaceInfoDto> placeInfo = requestDto.getPlaceInfo();
        boolean isConfirmPlace = requestDto.isConfirmPlace();

        if (isConfirmPlace && placeInfo.size() != 1) {
            throw new ConfirmPlaceCountException();
        }

        List<Place> placeList = placeInfo.stream()
                .map(placeInfoDto -> placeJpaRepository.findByMapxAndMapy(placeInfoDto.getMapx(), placeInfoDto.getMapy())
                        .orElseGet(() -> placeJpaRepository.save(placeInfoDto.toEntity())))
                .toList();

        placeList.forEach(place -> placeSlotJpaRepository.save(PlaceSlot.builder()
                .meet(meet)
                .confirm(isConfirmPlace)
                .place(place)
                .build()));
    }


}
