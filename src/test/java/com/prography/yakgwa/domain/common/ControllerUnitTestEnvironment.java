package com.prography.yakgwa.domain.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.domain.meet.service.MeetCreateService;
import com.prography.yakgwa.domain.meet.service.MeetService;
import com.prography.yakgwa.domain.meet.service.MeetThemeService;
import com.prography.yakgwa.domain.participant.service.ParticipantService;
import com.prography.yakgwa.domain.user.service.UserService;
import com.prography.yakgwa.domain.vote.service.VoteExecuter;
import com.prography.yakgwa.domain.vote.service.place.PlaceSlotService;
import com.prography.yakgwa.domain.vote.service.place.PlaceVoteExecuteService;
import com.prography.yakgwa.domain.vote.service.time.TimeVoteExecuteService;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.testHelper.mock.WithCustomMockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithCustomMockUser
public class ControllerUnitTestEnvironment {
    @Autowired
    protected MockMvc mvc;
    @MockBean
    protected MeetThemeService meetThemeService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected DummyCreater dummyCreater;
    @Autowired
    protected RepositoryDeleter deleter;
    @MockBean
    protected MeetService meetService;
    @MockBean
    protected MeetCreateService meetCreateService;
    @MockBean
    protected ParticipantService participantService;
    @MockBean
    protected UserService userService;
    @MockBean
    protected PlaceSlotService placeSlotService;
    @MockBean
    protected PlaceVoteExecuteService placeVoteExecuteService;
    @MockBean
    protected TimeVoteExecuteService timeVoteExecuteService;
}
