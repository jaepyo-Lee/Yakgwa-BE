package com.prography.yakgwa.domain.participant.service;

import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ParticipantServiceTest {
    @Autowired
    DummyCreater dummyCreater;
    @Autowired
    ParticipantService participantService;

    @Autowired
    ParticipantJpaRepository participantJpaRepository;
    @Autowired
    MeetJpaRepository meetJpaRepository;
    @Autowired
    MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    RepositoryDeleter deleter;

    @AfterEach
    void init(){
    deleter.deleteAll();
    }


    @Test
    void 모임참여여부() {
        // given

        User saveUser = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);
        // when
        System.out.println("=====Logic Start=====");

        Participant participant = participantService.enterMeet(saveUser2.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        List<Participant> allByMeetId = participantJpaRepository.findAllByMeetId(saveMeet.getId());
        assertAll(()-> assertThat(participant.getMeetRole()).isEqualTo(MeetRole.PARTICIPANT),
                ()-> assertThat(allByMeetId.size()).isEqualTo(2));
    }
}