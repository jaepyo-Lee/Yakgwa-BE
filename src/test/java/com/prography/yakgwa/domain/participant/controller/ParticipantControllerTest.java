package com.prography.yakgwa.domain.participant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.service.ParticipantService;
import com.prography.yakgwa.testHelper.mock.WithCustomMockUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ParticipantControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    ParticipantService participantService;
    @Autowired
    ObjectMapper objectMapper;

    @WithCustomMockUser
    @Test
    void 모임참가컨트롤러테스트() throws Exception {
        // given

        Participant participant = Participant.builder().id(1L).build();
        Long meetId = 1L;
        when(participantService.enterMeet(anyLong(),anyLong())).thenReturn(participant);

        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets/{meetId}",meetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(jsonPath("$.result.participantId").value(participant.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println("=====Logic End=====");
        // then
    }
}