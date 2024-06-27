package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.impl.PlaceReader;
import com.prography.yakgwa.domain.place.impl.PlaceWriter;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static java.lang.Boolean.TRUE;

@ImplService
@RequiredArgsConstructor
public class PlaceVoteWriter {
    private final PlaceVoteJpaRepository placeVoteJpaRepository;
    private final PlaceWriter placeWriter;
    private final PlaceReader placeReader;

    public PlaceVote write(User user, Meet meet, Place place, Boolean confirm) {
        PlaceVote placeVote = PlaceVote.builder()
                .confirm(confirm)
                .place(place)
                .voteCnt(0L)
                .meet(meet)
                .user(user)
                .build();

        return placeVoteJpaRepository.save(placeVote);
    }

    public void confirmAndWrite(User user, Meet meet, PlaceInfoDto placeInfo) {

        if (placeInfo == null) {
            return;
        }

        Optional<Place> maybePlace = placeReader.readByMapxAndMapy(placeInfo.getMapx(), placeInfo.getMapy());

        Place place = maybePlace.orElseGet(() -> placeWriter.write(placeInfo.toEntity()));

        write(user, meet, place, TRUE);
    }
}
