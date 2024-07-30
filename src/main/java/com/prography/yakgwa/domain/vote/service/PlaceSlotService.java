package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.res.PlaceSlotWithUserResponse;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.format.exception.slot.AlreadyAppendPlaceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class PlaceSlotService {
    private final MeetJpaRepository meetJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final PlaceSlotJpaRepository placeSlotJpaRepository;
    private final PlaceVoteJpaRepository placeVoteJpaRepository;

    /**
     * Todo
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:20
     * Finish-Date) 
     */
    public PlaceSlot appendSlotInMeet(Long meetId, PlaceInfoDto placeInfo) {
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meetId);
        boolean isExistSamePlaceSlot = placeSlots.stream()
                .noneMatch(placeSlot -> placeSlot.getPlace().getMapx().equals(placeInfo.getMapx()) &&
                        placeSlot.getPlace().getMapy().equals(placeInfo.getMapy()) &&
                        placeSlot.getPlace().getTitle().equals(placeInfo.getTitle()));
        if (!isExistSamePlaceSlot) {
            throw new AlreadyAppendPlaceException();
        }
        Place place = placeJpaRepository.findByMapxAndMapy(placeInfo.getMapx(), placeInfo.getMapy())
                .orElseGet(() -> placeJpaRepository.save(placeInfo.toEntity()));
        Meet meet = meetJpaRepository.findById(meetId)
                .orElseThrow(NotFoundMeetException::new);
        return placeSlotJpaRepository.save(PlaceSlot.builder()
                .meet(meet)
                .confirm(Boolean.FALSE)
                .place(place)
                .build());
    }

    /**
     * Todo
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:20
     * Finish-Date) 
     */
    public List<PlaceSlotWithUserResponse> findSlotInMeet(Long meetId) {
        List<PlaceSlotWithUserResponse> placeSlotWithUser = new ArrayList<>();
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meetId);
        for (PlaceSlot placeSlot : placeSlots) {
            List<PlaceVote> placeVotes = placeVoteJpaRepository.findAllByPlaceSlotId(placeSlot.getId());
            List<User> users = new ArrayList<>();
            for (PlaceVote placeVote : placeVotes) {
                users.add(placeVote.getUser());
            }
            PlaceSlotWithUserResponse placeSlotWithUserResponse = PlaceSlotWithUserResponse.builder()
                    .users(users).placeSlot(placeSlot)
                    .build();
            placeSlotWithUser.add(placeSlotWithUserResponse);
        }
        return placeSlotWithUser;
    }
}
