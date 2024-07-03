package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.impl.PlaceReader;
import com.prography.yakgwa.domain.place.impl.PlaceWriter;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.lang.Boolean.TRUE;

@ImplService
@RequiredArgsConstructor
public class PlaceVoteWriter {
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final PlaceWriter placeWriter;
    private final PlaceReader placeReader;
    private final PlaceSlotWriter placeSlotWriter;


    public PlaceVote write(User user, PlaceSlot placeSlot) {
        PlaceVote placeVote = PlaceVote.builder()
                .placeSlot(placeSlot)
                .user(user)
                .build();

        return placeVoteJpaRepository.save(placeVote);
    }

    public List<PlaceVote> writeAll(User user, List<PlaceSlot> placeSlots) {
        List<PlaceVote> placeVotes = placeSlots.stream().map(placeSlot -> PlaceVote.builder()
                        .placeSlot(placeSlot)
                        .user(user)
                        .build())
                .toList();
        return placeVoteJpaRepository.saveAll(placeVotes);
    }

    public void confirmAndWrite(Meet meet, PlaceInfoDto placeInfo) {
        if (placeInfo == null) {
            return;
        }

        Place place = placeReader.readByMapxAndMapy(placeInfo.getMapx(), placeInfo.getMapy())
                .orElseGet(() -> placeWriter.write(placeInfo.toEntity()));

        PlaceSlot placeSlot = PlaceSlot.builder().meet(meet).confirm(TRUE).place(place).build();
        placeSlotWriter.write(placeSlot);
    }


    public void deleteAllVoteOfUser(User user, List<PlaceSlot> placeSlotIdsInMeet){
        placeVoteJpaRepository.deleteAllByUserIdAndPlaceSlotIdIn(user, placeSlotIdsInMeet);
    }
}
