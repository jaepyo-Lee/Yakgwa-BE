package com.prography.yakgwa.domain.vote.controller;

import com.prography.yakgwa.domain.common.ControllerUnitTestEnvironment;
import com.prography.yakgwa.domain.vote.controller.req.ConfirmTimeVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.req.EnableTimeRequest;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class TimeVoteControllerTest extends ControllerUnitTestEnvironment {
    @Test
    void 시간투표api테스트() throws Exception {
        // given

        LocalDateTime now = LocalDateTime.now();
        // "yyyy-MM-dd HH" 형식의 DateTimeFormatter 객체 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // LocalDateTime 객체를 문자열로 포맷팅
        String formattedDateTime = now.format(formatter);
        LocalDateTime parse = LocalDateTime.parse(formattedDateTime, formatter);
        TimeVote timeVote = new TimeVote();

        when(timeVoteExecuteService.vote(anyLong(), anyLong(), any())).thenReturn(List.of(timeVote));

        EnableTimeRequest request = EnableTimeRequest.of(List.of(parse));

        long meetId = 1L;

        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets/{meetId}/times", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());


        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 시간투표api테스트_시간형식에맞지않을떄예외() throws Exception {
        // given

        LocalDateTime now = LocalDateTime.now();
        // "yyyy-MM-dd HH" 형식의 DateTimeFormatter 객체 생성

        // LocalDateTime 객체를 문자열로 포맷팅
        TimeVote timeVote = new TimeVote();

        when(timeVoteExecuteService.vote(anyLong(), anyLong(), any())).thenReturn(List.of(timeVote));

        long meetId = 1L;
        String request = String.format("{\"enableTimes\":[{\"enableTime\":\"%s}\"}]}", now);
        // when
        System.out.println("=====Logic Start=====");
        String s = objectMapper.writeValueAsString(request);
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets/{meetId}/times", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 시간투표api테스트_요청값이없을때예외() throws Exception {
        // given

        LocalDateTime now = LocalDateTime.now();
        // "yyyy-MM-dd HH" 형식의 DateTimeFormatter 객체 생성

        // LocalDateTime 객체를 문자열로 포맷팅
        TimeVote timeVote = new TimeVote();

        when(timeVoteExecuteService.vote(anyLong(), anyLong(), any())).thenReturn(List.of(timeVote));

        EnableTimeRequest request = EnableTimeRequest.of(List.of(now));

        long meetId = 1L;

        // when
        System.out.println("=====Logic Start=====");
        String s = objectMapper.writeValueAsString(request);
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets/{meetId}/times", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("=====Logic End=====");
        // then
    }

    /**
     * @Override
     * @PatchMapping("/meets/{meetId}/times/confirm") public SuccessResponse<String> confirmTimeInMeet(@AuthenticationPrincipal CustomUserDetail user,
     * @PathVariable("meetId") Long meetId,
     * @RequestBody @Valid ConfirmTimeVoteInMeetRequest request) throws JsonProcessingException {
     * voteExecuter.confirm(meetId, user.getUserId(), request.getConfirmTimeSlotId());
     * return SuccessResponse.ok("시간이 확정되었습니다");
     * }
     */

    @Test
    void 모임시간후보확정api테스트() throws Exception {
        // given
        doNothing().when(timeVoteExecuteService).confirm(anyLong(), anyLong(), anyLong());
        long meetId = 1L;
        long confirmTimeSlotId = 1L;
        ConfirmTimeVoteInMeetRequest request = ConfirmTimeVoteInMeetRequest.builder().confirmTimeSlotId(confirmTimeSlotId).build();
        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.patch("/api/v1/meets/{meetId}/times/confirm", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 모임시간후보확정api_시간후보ID안넣었을경우_예외테스트() throws Exception {
        // given
        doNothing().when(timeVoteExecuteService).confirm(anyLong(), anyLong(), anyLong());
        long meetId = 1L;
        long confirmTimeSlotId = 1L;
        ConfirmTimeVoteInMeetRequest request = ConfirmTimeVoteInMeetRequest.builder().build();
        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.patch("/api/v1/meets/{meetId}/times/confirm", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("=====Logic End=====");
        // then
    }
}