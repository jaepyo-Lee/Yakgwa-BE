package com.prography.yakgwa.domain.meet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.schedule.TaskScheduleManager;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetConfirmChecker;
import com.prography.yakgwa.domain.meet.impl.MeetWriter;
import com.prography.yakgwa.domain.meet.service.req.MeetCreateRequestDto;
import com.prography.yakgwa.domain.participant.impl.ParticipantWriter;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.format.exception.meet.ConfirmPlaceCountException;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetCreateService {
    private final TaskScheduleManager alarmScheduler;
    private final MeetWriter meetWriter;
    private final ParticipantWriter participantWriter;
    private final UserJpaRepository userJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final MeetConfirmChecker confirmChecker;
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
    public Meet create(MeetCreateRequestDto requestDto) throws JsonProcessingException {
        User user = userJpaRepository.findById(requestDto.getCreatorId())
                .orElseThrow(NotFoundUserException::new);
        Meet meet = meetWriter.write(requestDto.toMeetWriteDto());

        savePlaceSlotOf(meet, requestDto);
        saveTimeSlotOf(meet, requestDto);

        participantWriter.registLeader(meet, user);

        registAlarm(meet);
        return meet;
    }

    private void registAlarm(Meet meet) {
        if (confirmChecker.isMeetConfirm(meet)) {
            alarmScheduler.registerAlarm(meet, AlarmType.PROMISE_DAY);
        }
        else{
            alarmScheduler.registerAlarm(meet, AlarmType.END_VOTE);
        }
    }

    private void saveTimeSlotOf(Meet meet, MeetCreateRequestDto requestDto) {
        if (requestDto.getMeetTime() == null) {
            return;
        }
        TimeSlot timeSlot = TimeSlot.ofConfirm(meet, requestDto.getMeetTime());
        timeSlotJpaRepository.save(timeSlot);
    }

    private void savePlaceSlotOf(Meet meet, MeetCreateRequestDto requestDto) {
        List<PlaceInfoDto> placeInfo = requestDto.getPlaceInfo();
        boolean isConfirmPlace = requestDto.isConfirmPlace();

        if (isConfirmPlace && placeInfo.size() != 1) {
            throw new ConfirmPlaceCountException();
        }

        List<Place> places = placeInfo.stream()
                .map(this::findOrSavePlace)
                .toList();

        List<PlaceSlot> placeSlots = places.stream()
                .map(place -> createPlaceSlot(meet, isConfirmPlace, place))
                .toList();

        placeSlotJpaRepository.saveAll(placeSlots);
    }

    private Place findOrSavePlace(PlaceInfoDto placeInfoDto) {
        return placeJpaRepository.findByMapxAndMapy(placeInfoDto.getMapx(), placeInfoDto.getMapy())
                .orElseGet(() -> placeJpaRepository.save(placeInfoDto.toEntity()));
    }

    private PlaceSlot createPlaceSlot(Meet meet, boolean isConfirmPlace, Place place) {
        return PlaceSlot.builder()
                .meet(meet)
                .confirm(isConfirmPlace)
                .place(place)
                .build();
    }
}
