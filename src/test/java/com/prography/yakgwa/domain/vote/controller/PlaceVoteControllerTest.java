package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.common.ControllerUnitTestEnvironment;
import com.prography.yakgwa.domain.vote.controller.req.ConfirmPlaceVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.VotePlaceRequest;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class PlaceVoteControllerTest extends ControllerUnitTestEnvironment {

    @Test
    void placeVote장소투표컨트롤러테스트() throws Exception {
        Long meetId = 1L;
        PlaceVote placeVote = new PlaceVote();
        when(placeVoteExecuteService.vote(anyLong(), anyLong(), anySet())).thenReturn(List.of(placeVote));

        VotePlaceRequest request = VotePlaceRequest.builder().currentVotePlaceSlotIds(Set.of(1L, 2L)).build();
        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets/{meetId}/places", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println("=====Logic End=====");
        // then
    }

    /**
     * Todo
     * Work) 파라미터도 응답값을 커스텀해서 보내주면 좋을것 같음
     * Write-Date)
     * Finish-Date)
     */
    @Test
    void 요청값이null일때_placeVote장소투표컨트롤러_예외테스트() throws Exception {
        Long meetId = 1L;
        PlaceVote placeVote = new PlaceVote();
        when(placeVoteExecuteService.vote(anyLong(), anyLong(), anySet())).thenReturn(List.of(placeVote));

        VotePlaceRequest request = VotePlaceRequest.builder().currentVotePlaceSlotIds(null).build();
        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets/{meetId}/places", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 장소확정짓기confirmPlaceInMeet컨트롤러테스트() throws Exception {
        Long meetId = 1L;
        doNothing().when(placeVoteExecuteService).confirm(anyLong(), anyLong(), anyLong());
        ConfirmPlaceVoteInMeetRequest request = ConfirmPlaceVoteInMeetRequest.builder().confirmPlaceSlotId(1L).build();
        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.patch("/api/v1/meets/{meetId}/places/confirm", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 요청값이null인경우_장소확정짓기confirmPlaceInMeet컨트롤러테스트() throws Exception {
        Long meetId = 1L;
        doNothing().when(placeVoteExecuteService).confirm(anyLong(), anyLong(), anyLong());
        ConfirmPlaceVoteInMeetRequest request = ConfirmPlaceVoteInMeetRequest.builder().confirmPlaceSlotId(null).build();
        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.patch("/api/v1/meets/{meetId}/places/confirm", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("=====Logic End=====");
        // then
    }
}