package com.prography.yakgwa.domain.vote.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.prography.yakgwa.domain.common.ControllerUnitTestEnvironment;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.vote.controller.res.PlaceVoteInfoWithStatusResponse;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.service.place.res.PlaceInfosByMeetStatus;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class PlaceVoteFindControllerTest extends ControllerUnitTestEnvironment {
    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 사용자의상황에따른투표정보호출api테스트() throws Exception {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        PlaceInfosByMeetStatus infos = PlaceInfosByMeetStatus.of(VoteStatus.VOTE, List.of(savePlaceSlot));

        when(placeVoteFindService.findVoteInfoWithStatusOf(anyLong(), anyLong())).thenReturn(infos);

        PlaceVoteInfoWithStatusResponse notYet = PlaceVoteInfoWithStatusResponse.of(infos.getVoteStatus(), infos.getPlaces());
        String serialize = objectMapper.writeValueAsString(notYet);
        PlaceVoteInfoWithStatusResponse compare = objectMapper.readValue(serialize, PlaceVoteInfoWithStatusResponse.class);
        // when
        System.out.println("=====Logic Start=====");

        long meetId = 1L;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/v1/meets/{meetId}/places", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        System.out.println("=====Logic End=====");
        // then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        SuccessResponse<PlaceVoteInfoWithStatusResponse> response = objectMapper.readValue(contentAsString, new TypeReference<SuccessResponse<PlaceVoteInfoWithStatusResponse>>() {
        });

        assertThat(response.getResult()).usingRecursiveComparison().isEqualTo(compare);

    }

}