package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlaceConfirmTest extends IntegrationTestSupport {
    @AfterEach
    void init(){
        deleter.deleteAll();
    }
    @Test
    void 투표를가장많이받은장소를확정짓는다() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);
        User saveUser3 = dummyCreater.createAndSaveUser(3);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser3, MeetRole.PARTICIPANT);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        Place savePlace3 = dummyCreater.createAndSavePlace(3);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);
        PlaceSlot savePlaceSlot3 = dummyCreater.createAndSavePlaceSlot(savePlace3, saveMeet, false);
        dummyCreater.createAndSavePlaceVote(saveUser,savePlaceSlot);
        dummyCreater.createAndSavePlaceVote(saveUser2,savePlaceSlot);
        dummyCreater.createAndSavePlaceVote(saveUser3,savePlaceSlot);
        dummyCreater.createAndSavePlaceVote(saveUser,savePlaceSlot2);
        dummyCreater.createAndSavePlaceVote(saveUser3,savePlaceSlot2);
        // savePlaceSlot이 최댓 투표

        // when
        System.out.println("=====Logic Start=====");

        boolean confirmMaxOf = placeConfirm.confirmMaxOf(saveMeet);

        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(confirmMaxOf).isTrue(),
                ()-> assertThat(savePlaceSlot.isConfirm()).isTrue(),
                ()-> assertThat(savePlaceSlot2.isConfirm()).isFalse(),
                ()-> assertThat(savePlaceSlot3.isConfirm()).isFalse());
    }

    @Test
    void 동표를받은경우() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);
        User saveUser3 = dummyCreater.createAndSaveUser(3);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser3, MeetRole.PARTICIPANT);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        Place savePlace3 = dummyCreater.createAndSavePlace(3);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);
        PlaceSlot savePlaceSlot3 = dummyCreater.createAndSavePlaceSlot(savePlace3, saveMeet, false);
        dummyCreater.createAndSavePlaceVote(saveUser,savePlaceSlot);
        dummyCreater.createAndSavePlaceVote(saveUser2,savePlaceSlot);
        dummyCreater.createAndSavePlaceVote(saveUser,savePlaceSlot2);
        dummyCreater.createAndSavePlaceVote(saveUser3,savePlaceSlot2);

        // when
        System.out.println("=====Logic Start=====");

        boolean confirmMaxOf = placeConfirm.confirmMaxOf(saveMeet);
        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(confirmMaxOf).isFalse(),
                ()-> assertThat(savePlaceSlot.isConfirm()).isFalse(),
                ()-> assertThat(savePlaceSlot2.isConfirm()).isFalse(),
                ()-> assertThat(savePlaceSlot3.isConfirm()).isFalse()
                );
    }

}