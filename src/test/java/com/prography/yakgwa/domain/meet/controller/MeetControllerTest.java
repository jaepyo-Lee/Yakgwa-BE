package com.prography.yakgwa.domain.meet.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.domain.common.ControllerUnitTestEnvironment;
import com.prography.yakgwa.domain.meet.controller.res.MeetInfoWithParticipantResponse;
import com.prography.yakgwa.domain.meet.controller.res.MeetWithStatusInfoResponse;
import com.prography.yakgwa.domain.meet.controller.res.PostConfirmMeetInfoResponse;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.service.MeetService;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.meet.service.res.MeetInfoWithParticipant;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.testHelper.mock.WithCustomMockUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class MeetControllerTest extends ControllerUnitTestEnvironment {
    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 모임세부정보조회컨트롤러() throws Exception {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);

        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);
        User saveUser3 = dummyCreater.createAndSaveUser(3);

        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        Participant saveParticipant1 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser3, MeetRole.PARTICIPANT);

        List<Participant> saveParticipants = List.of(saveParticipant, saveParticipant1, saveParticipant2);
        MeetInfoWithParticipant serviceMockReturnValue = MeetInfoWithParticipant.of(saveMeet, saveParticipants);

        when(meetService.findWithParticipant(anyLong())).thenReturn(serviceMockReturnValue);

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/v1/meets/{meetId}", saveMeet.getId())
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // then
        SuccessResponse<MeetInfoWithParticipantResponse> meetInfoWithParticipantResponse =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), new TypeReference<SuccessResponse<MeetInfoWithParticipantResponse>>() {
                        });

        MeetInfoWithParticipantResponse result = meetInfoWithParticipantResponse.getResult();
        MeetInfoWithParticipantResponse compareResult = MeetInfoWithParticipantResponse.of(saveMeet, saveParticipants);

        assertThat(result).usingRecursiveComparison().isEqualTo(compareResult);
    }


    /**
     * @GetMapping("/meets") public SuccessResponse<MeetWithStatusInfoResponse> findCurrentMeetsForUser(@AuthenticationPrincipal CustomUserDetail user) {
     * List<MeetWithVoteAndStatus> meetWithVoteAndStatuses = meetService.findWithStatus(user.getUserId());
     * return new SuccessResponse<>(MeetWithStatusInfoResponse.of(meetWithVoteAndStatuses));
     * }
     */
    @Test
    void 현재참여중인모임조회컨트롤러() throws Exception {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet1 = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet1, true);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet1, LocalDateTime.now(), true);

        Meet saveMeet2 = dummyCreater.createAndSaveMeet(2, saveMeetTheme, 24);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet2, true);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet2, LocalDateTime.now(), false);

        User saveUser = dummyCreater.createAndSaveUser(1);

        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet1, saveUser, MeetRole.LEADER);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet2, saveUser, MeetRole.PARTICIPANT);

        MeetWithVoteAndStatus meetWithVoteAndStatus = MeetWithVoteAndStatus.of(saveMeet1, saveTimeSlot1, savePlaceSlot1, MeetStatus.CONFIRM);
        MeetWithVoteAndStatus meetWithVoteAndStatus1 = MeetWithVoteAndStatus.of(saveMeet2, saveTimeSlot2, savePlaceSlot2, MeetStatus.BEFORE_VOTE);
        List<MeetWithVoteAndStatus> meetWithVoteAndStatuses = List.of(meetWithVoteAndStatus, meetWithVoteAndStatus1);

        when(meetService.findWithStatus(anyLong())).thenReturn(meetWithVoteAndStatuses);

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/v1/meets")
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // then
        SuccessResponse<MeetWithStatusInfoResponse>  meetInfoWithParticipantResponse =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), new TypeReference<SuccessResponse<MeetWithStatusInfoResponse> >() {
                        });

        MeetWithStatusInfoResponse result = meetInfoWithParticipantResponse.getResult();

        MeetWithStatusInfoResponse compareResult = MeetWithStatusInfoResponse.of(meetWithVoteAndStatuses);

        assertThat(result).usingRecursiveComparison().isEqualTo(compareResult);
    }

    @Test
    void 확정또는이미지난모임들조회컨트롤러테스트() throws Exception {
        // given

        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet1 = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot1 = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet1, true);
        TimeSlot saveTimeSlot1 = dummyCreater.createAndSaveTimeSlot(saveMeet1, LocalDateTime.now(), true);

        Meet saveMeet2 = dummyCreater.createAndSaveMeet(2, saveMeetTheme, 24);
        PlaceSlot savePlaceSlot2 = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet2, true);
        TimeSlot saveTimeSlot2 = dummyCreater.createAndSaveTimeSlot(saveMeet2, LocalDateTime.now(), false);

        User saveUser = dummyCreater.createAndSaveUser(1);

        Participant saveParticipant = dummyCreater.createAndSaveParticipant(saveMeet1, saveUser, MeetRole.LEADER);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet2, saveUser, MeetRole.PARTICIPANT);

        MeetWithVoteAndStatus meetWithVoteAndStatus = MeetWithVoteAndStatus.of(saveMeet1, saveTimeSlot1, savePlaceSlot1, MeetStatus.CONFIRM);
        MeetWithVoteAndStatus meetWithVoteAndStatus1 = MeetWithVoteAndStatus.of(saveMeet2, saveTimeSlot2, savePlaceSlot2, MeetStatus.BEFORE_VOTE);
        List<MeetWithVoteAndStatus> meetWithVoteAndStatuses = List.of(meetWithVoteAndStatus, meetWithVoteAndStatus1);

        when(meetService.findPostConfirm(anyLong())).thenReturn(meetWithVoteAndStatuses);

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/v1/meets/record")
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // then
        SuccessResponse<PostConfirmMeetInfoResponse>  meetInfoWithParticipantResponse =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), new TypeReference<SuccessResponse<PostConfirmMeetInfoResponse> >() {
                        });

        PostConfirmMeetInfoResponse result = meetInfoWithParticipantResponse.getResult();

        PostConfirmMeetInfoResponse compareResult = PostConfirmMeetInfoResponse.of(meetWithVoteAndStatuses);

        assertThat(result).usingRecursiveComparison().isEqualTo(compareResult);
        // then
    }
}