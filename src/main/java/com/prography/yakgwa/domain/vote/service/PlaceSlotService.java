package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetReader;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.impl.PlaceReader;
import com.prography.yakgwa.domain.place.impl.PlaceWriter;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.impl.PlaceSlotReader;
import com.prography.yakgwa.domain.vote.impl.PlaceSlotWriter;
import com.prography.yakgwa.global.format.exception.slot.AlreadyAppendPlaceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class PlaceSlotService {
    private final PlaceSlotReader placeSlotReader;
    private final PlaceWriter placeWriter;
    private final PlaceReader placeReader;
    private final PlaceSlotWriter placeSlotWriter;
    private final MeetReader meetReader;

    public PlaceSlot appendSlotInMeet(Long meetId, PlaceInfoDto placeInfo) {
        List<PlaceSlot> placeSlots = placeSlotReader.readAllByMeetId(meetId);
        boolean isExistSamePlaceSlot = placeSlots.stream()
                .noneMatch(placeSlot -> placeSlot.getPlace().getMapx().equals(placeInfo.getMapx()) &&
                        placeSlot.getPlace().getMapy().equals(placeInfo.getMapy()) &&
                        placeSlot.getPlace().getTitle().equals(placeInfo.getTitle()));
        if (!isExistSamePlaceSlot) {
            throw new AlreadyAppendPlaceException();
        }
        Place place = placeReader.readByMapxAndMapy(placeInfo.getMapx(), placeInfo.getMapy())
                .orElseGet(() -> placeWriter.write(placeInfo.toEntity()));
        Meet meet = meetReader.read(meetId);
        return placeSlotWriter.write(PlaceSlot.builder()
                .meet(meet)
                .confirm(Boolean.FALSE)
                .place(place)
                .build());

    }
}
