package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.vote.service.place.PlaceSlotService;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.service.place.res.PlaceSlotWithUserResponse;
import com.prography.yakgwa.global.format.exception.slot.AlreadyAppendPlaceException;
import com.prography.yakgwa.global.format.exception.vote.AlreadyPlaceConfirmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlaceSlotServiceTest extends IntegrationTestSupport {


    @BeforeEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 모임장소후보_추가() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        PlaceInfoDto placeInfoDto = PlaceInfoDto.builder().mapx("123").mapy("123").title("test").build();
        // when
        System.out.println("=====Logic Start=====");

        placeSlotService.appendPlaceSlotFrom(saveMeet.getId(), placeInfoDto);

        System.out.println("=====Logic End=====");
        // then
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(saveMeet.getId());
        assertThat(placeSlots.size()).isEqualTo(1);
    }

    @Test
    void 이미모임장소가확정되었을때_예외() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        PlaceInfoDto placeInfoDto = PlaceInfoDto.builder().mapx("123").mapy("123").title("test").build();
        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(AlreadyPlaceConfirmException.class, () -> placeSlotService.appendPlaceSlotFrom(saveMeet.getId(), placeInfoDto));
        System.out.println("=====Logic End=====");
    }

    @Test
    void 이미추가된모임장소일때_예외() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        PlaceInfoDto placeInfoDto = PlaceInfoDto.builder().mapx(savePlace.getMapx()).mapy(savePlace.getMapy()).title(savePlace.getTitle()).build();
        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(AlreadyAppendPlaceException.class, () -> placeSlotService.appendPlaceSlotFrom(saveMeet.getId(), placeInfoDto));
        System.out.println("=====Logic End=====");
    }

    @Test
    void 해당모임에추가되어있는모임장소_조회() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace1 = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        Place savePlace3 = dummyCreater.createAndSavePlace(3);
        dummyCreater.createAndSavePlaceSlot(savePlace1, saveMeet, false);
        dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);
        dummyCreater.createAndSavePlaceSlot(savePlace3, saveMeet, false);
        // when
        System.out.println("=====Logic Start=====");

        List<PlaceSlotWithUserResponse> placeSlotFrom = placeSlotService.findPlaceSlotFrom(saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertThat(placeSlotFrom.size()).isEqualTo(3);

    }

    @Test
    void 모임장소가이미확정되었을때_예외() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace1 = dummyCreater.createAndSavePlace(1);
        Place savePlace2 = dummyCreater.createAndSavePlace(2);
        Place savePlace3 = dummyCreater.createAndSavePlace(3);
        dummyCreater.createAndSavePlaceSlot(savePlace1, saveMeet, true);
        dummyCreater.createAndSavePlaceSlot(savePlace2, saveMeet, false);
        dummyCreater.createAndSavePlaceSlot(savePlace3, saveMeet, false);
        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThrows(AlreadyPlaceConfirmException.class,()->placeSlotService.findPlaceSlotFrom(saveMeet.getId()));

        System.out.println("=====Logic End=====");

    }
}