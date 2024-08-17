package com.prography.yakgwa.domain.vote.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.prography.yakgwa.domain.common.ControllerUnitTestEnvironment;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.user.controller.res.UserInfoResponse;
import com.prography.yakgwa.domain.vote.controller.req.ConfirmTimeVoteInMeetRequest;
import com.prography.yakgwa.domain.vote.controller.res.TimeVoteInfoWithStatusResponse;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.service.time.res.TimeInfosByMeetStatus;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class TimeVoteFindControllerTest extends ControllerUnitTestEnvironment {
    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 사용자의상황에따른투표정보호출api테스트() throws Exception {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);

        TimeInfosByMeetStatus timeInfo = TimeInfosByMeetStatus.of(VoteStatus.VOTE, List.of(saveTimeSlot), saveMeet);

        when(timeVoteFindService.findVoteInfoWithStatusOf(anyLong(), anyLong())).thenReturn(timeInfo);

        TimeVoteInfoWithStatusResponse notYet = TimeVoteInfoWithStatusResponse.of(timeInfo.getVoteStatus(), timeInfo.getTimeSlots(), timeInfo.getMeet());
        String serialize = objectMapper.writeValueAsString(notYet);
        TimeVoteInfoWithStatusResponse compare = objectMapper.readValue(serialize, TimeVoteInfoWithStatusResponse.class);
        // when
        System.out.println("=====Logic Start=====");

        long meetId = 1L;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/v1/meets/{meetId}/times", meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        System.out.println("=====Logic End=====");
        // then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        SuccessResponse<TimeVoteInfoWithStatusResponse> response = objectMapper.readValue(contentAsString, new TypeReference<SuccessResponse<TimeVoteInfoWithStatusResponse>>() {
        });

        assertThat(response.getResult()).usingRecursiveComparison().isEqualTo(compare);
    }
}