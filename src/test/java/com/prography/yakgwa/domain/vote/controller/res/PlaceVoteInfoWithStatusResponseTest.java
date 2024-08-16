package com.prography.yakgwa.domain.vote.controller.res;

import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

class PlaceVoteInfoWithStatusResponseTest {
    @Test
    void placeSlot이비지않았을때() {
        // given
        VoteStatus voteStatus = VoteStatus.BEFORE_VOTE;
        PlaceSlot mock = Mockito.mock(PlaceSlot.class);
        List<PlaceSlot> list = List.of(mock);
        long placeId = 1L;
        String roadAddress = "roadAddress";
        String title = "title";
        String mapy = "mapy";
        String mapx = "mapx";
        Place place = Place.builder()
                .id(placeId)
                .roadAddress(roadAddress)
                .title(title)
                .mapy(mapy)
                .mapx(mapx)
                .build();
        when(mock.getPlace()).thenReturn(place);
        long mockId = 1L;
        when(mock.getId()).thenReturn(mockId);

        PlaceVoteInfoWithStatusResponse.VotePlaceInfo votePlaceInfo = PlaceVoteInfoWithStatusResponse.VotePlaceInfo.builder()
                .placeSlotId(mockId)
                .mapy(mapy)
                .mapx(mapx)
                .roadAddress(roadAddress)
                .title(title)
                .build();
        List<PlaceVoteInfoWithStatusResponse.VotePlaceInfo> expect = List.of(votePlaceInfo);

        // when
        System.out.println("=====Logic Start=====");

        PlaceVoteInfoWithStatusResponse response = PlaceVoteInfoWithStatusResponse.of(voteStatus, list);
        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(response.getMeetStatus()).isEqualTo(voteStatus),
                () -> assertThat(response.getPlaceInfos().size()).isOne(),
                () -> assertThat(response.getPlaceInfos()).usingRecursiveComparison().isEqualTo(expect));
    }

    @Test
    void placeSlot이비었을때() {
        // given
        VoteStatus voteStatus = VoteStatus.BEFORE_VOTE;
        List<PlaceSlot> list = List.of();

        // when
        System.out.println("=====Logic Start=====");

        PlaceVoteInfoWithStatusResponse response = PlaceVoteInfoWithStatusResponse.of(voteStatus, list);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(response.getMeetStatus()).isEqualTo(voteStatus),
                () -> assertThat(response.getPlaceInfos()).isNull());
    }
}