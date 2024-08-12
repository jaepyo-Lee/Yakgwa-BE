package com.prography.yakgwa.domain.meet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prography.yakgwa.domain.common.schedule.TaskScheduleManager;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.service.dto.VoteDateDto;
import com.prography.yakgwa.domain.meet.service.req.MeetCreateRequestDto;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class MeetCreateServiceTest {
    @Autowired
    DummyCreater dummyCreater;

    @Autowired
    MeetCreateService meetCreateService;
    @Autowired
    private PlaceSlotJpaRepository placeSlotJpaRepository;
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    private ParticipantJpaRepository participantJpaRepository;

    @Autowired
    RepositoryDeleter deleter;
    @MockBean
    TaskScheduleManager scheduler;

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 시간만확정된경우_모임생성() throws JsonProcessingException {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        String title = "title";
        LocalDateTime confirmTime = LocalDateTime.now();
        String description = "description";
        MeetCreateRequestDto createRequestDto = MeetCreateRequestDto.builder()
                .title(title).meetTime(confirmTime).meetThemeId(saveMeetTheme.getId()).description(description).creatorId(saveUser.getId()).voteDateDto(null).placeInfo(List.of()).confirmPlace(false)
                .build();
        doNothing().when(scheduler).registerAlarm(any(), any());

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetCreateService.create(createRequestDto);

        System.out.println("=====Logic End=====");
        // then
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meet.getId());
        List<TimeSlot> allByMeetId = timeSlotJpaRepository.findAllByMeetId(meet.getId());
        List<Participant> allParticipant = participantJpaRepository.findAllByMeetId(meet.getId());
        assertAll(() -> assertThat(meet.getPeriod()).isNull(),
                () -> assertThat(meet.getTitle()).isEqualTo(title),
                () -> assertThat(meet.getMeetTheme().getId()).isEqualTo(saveMeetTheme.getId()),
                () -> assertThat(meet.getDescription()).isEqualTo(description),
                () -> assertThat(meet.getValidInviteHour()).isEqualTo(24),
                () -> assertThat(placeSlots.size()).isEqualTo(0),
                () -> assertThat(allByMeetId.size()).isEqualTo(1),
                () -> assertThat(allByMeetId.stream()
                        .filter(TimeSlot::getConfirm)
                        .toList().size()).isEqualTo(1),
                () -> assertThat(allParticipant.size()).isEqualTo(1),
                () -> assertThat(allParticipant.stream()
                        .filter(participant -> participant.getMeetRole().equals(MeetRole.LEADER))
                        .toList().size()).isEqualTo(1)
        );
        verify(scheduler, times(1)).registerAlarm(any(), eq(AlarmType.END_VOTE));
        verify(scheduler, never()).registerAlarm(any(), eq(AlarmType.PROMISE_DAY));


    }

    @Test
    void 장소만확정된경우_모임생성() throws JsonProcessingException {
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        String title = "title";
        String description = "description";
        PlaceInfoDto placeInfoDto = PlaceInfoDto.builder()
                .description("placedes").mapx("mapx").mapy("mapy").link("link").telephone("telephone").category("category").roadAddress("roadAddress").title("title").address("address")
                .build();
        LocalDate startVoteDate = LocalDate.now();
        LocalDate endVoteDate = LocalDate.now().plusDays(1);
        VoteDateDto voteDateDto = VoteDateDto.builder().startVoteDate(startVoteDate).endVoteDate(endVoteDate).build();
        MeetCreateRequestDto createRequestDto = MeetCreateRequestDto.builder()
                .title(title).meetTime(null).meetThemeId(saveMeetTheme.getId()).description(description).creatorId(saveUser.getId()).voteDateDto(voteDateDto).placeInfo(List.of(placeInfoDto)).confirmPlace(true)
                .build();
        doNothing().when(scheduler).registerAlarm(any(), any());

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetCreateService.create(createRequestDto);

        System.out.println("=====Logic End=====");
        // then
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meet.getId());
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllByMeetId(meet.getId());
        List<Participant> participants = participantJpaRepository.findAllByMeetId(meet.getId());

        assertAll(
                () -> assertThat(meet.getTitle()).isEqualTo(title),
                () -> assertThat(meet.getPeriod().getStartDate()).isEqualTo(startVoteDate),
                () -> assertThat(meet.getPeriod().getEndDate()).isEqualTo(endVoteDate),
                () -> assertThat(placeSlots.size()).isEqualTo(1),
                () -> assertThat(placeSlots.stream().filter(PlaceSlot::getConfirm).toList().size()).isEqualTo(1),
                () -> assertThat(timeSlots.size()).isEqualTo(0),
                () -> assertThat(participants.size()).isEqualTo(1),
                () -> assertThat(participants.stream()
                        .filter(participant -> participant.getMeetRole().equals(MeetRole.LEADER))
                        .toList().size()).isEqualTo(1)
        );
        verify(scheduler, times(1)).registerAlarm(any(), eq(AlarmType.END_VOTE));
        verify(scheduler, never()).registerAlarm(any(), eq(AlarmType.PROMISE_DAY));
    }

    @Test
    void 시간과장소모두확정된경우_모임생성() throws JsonProcessingException {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        String title = "title";
        String description = "description";
        PlaceInfoDto placeInfoDto = PlaceInfoDto.builder()
                .description("placedes").mapx("mapx").mapy("mapy").link("link").telephone("telephone").category("category").roadAddress("roadAddress").title("title").address("address")
                .build();
        LocalDateTime confirmTime = LocalDateTime.now();
        LocalDate startVoteDate = LocalDate.now();
        LocalDate endVoteDate = LocalDate.now().plusDays(1);
        VoteDateDto voteDateDto = VoteDateDto.builder().startVoteDate(startVoteDate).endVoteDate(endVoteDate).build();
        MeetCreateRequestDto createRequestDto = MeetCreateRequestDto.builder()
                .title(title).meetTime(confirmTime).meetThemeId(saveMeetTheme.getId()).description(description).creatorId(saveUser.getId()).voteDateDto(null).placeInfo(List.of(placeInfoDto)).confirmPlace(true)
                .build();
        doNothing().when(scheduler).registerAlarm(any(), eq(AlarmType.END_VOTE));

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetCreateService.create(createRequestDto);

        System.out.println("=====Logic End=====");

        // then
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meet.getId());
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllByMeetId(meet.getId());
        List<Participant> allParticipant = participantJpaRepository.findAllByMeetId(meet.getId());
        assertAll(() -> assertThat(meet.getPeriod()).isNull(),
                () -> assertThat(meet.getTitle()).isEqualTo(title),
                () -> assertThat(meet.getMeetTheme().getId()).isEqualTo(saveMeetTheme.getId()),
                () -> assertThat(meet.getDescription()).isEqualTo(description),
                () -> assertThat(meet.getValidInviteHour()).isEqualTo(24),
                () -> assertThat(placeSlots.size()).isEqualTo(1),
                () -> assertThat(placeSlots.stream()
                        .filter(PlaceSlot::getConfirm)
                        .toList().size()).isEqualTo(1),
                () -> assertThat(timeSlots.size()).isEqualTo(1),
                () -> assertThat(timeSlots.stream()
                        .filter(TimeSlot::getConfirm)
                        .toList().size()).isEqualTo(1),
                () -> assertThat(allParticipant.size()).isEqualTo(1),
                () -> assertThat(allParticipant.stream()
                        .filter(participant -> participant.getMeetRole().equals(MeetRole.LEADER))
                        .toList().size()).isEqualTo(1)
        );
        verify(scheduler, never()).registerAlarm(any(), eq(AlarmType.END_VOTE));
        verify(scheduler, times(1)).registerAlarm(any(), eq(AlarmType.PROMISE_DAY));
    }

    @Test
    void 시간과장소모두확정안된경우_모임생성() throws JsonProcessingException {
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        String title = "title";
        String description = "description";
        PlaceInfoDto placeInfoDto = PlaceInfoDto.builder()
                .description("placedes").mapx("mapx").mapy("mapy").link("link").telephone("telephone").category("category").roadAddress("roadAddress").title("title").address("address")
                .build();
        LocalDate startVoteDate = LocalDate.now();
        LocalDate endVoteDate = LocalDate.now().plusDays(1);
        VoteDateDto voteDateDto = VoteDateDto.builder().startVoteDate(startVoteDate).endVoteDate(endVoteDate).build();

        MeetCreateRequestDto createRequestDto = MeetCreateRequestDto.builder()
                .title(title).meetTime(null).meetThemeId(saveMeetTheme.getId()).description(description).creatorId(saveUser.getId()).voteDateDto(voteDateDto).placeInfo(List.of(placeInfoDto)).confirmPlace(false)
                .build();
        doNothing().when(scheduler).registerAlarm(any(), eq(AlarmType.END_VOTE));

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetCreateService.create(createRequestDto);

        System.out.println("=====Logic End=====");
        // then
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meet.getId());
        List<TimeSlot> timeSlots = timeSlotJpaRepository.findAllByMeetId(meet.getId());
        List<Participant> participants = participantJpaRepository.findAllByMeetId(meet.getId());

        assertAll(
                () -> assertThat(meet.getTitle()).isEqualTo(title),
                () -> assertThat(meet.getPeriod().getStartDate()).isEqualTo(startVoteDate),
                () -> assertThat(meet.getPeriod().getEndDate()).isEqualTo(endVoteDate),
                () -> assertThat(placeSlots.size()).isEqualTo(1),
                () -> assertThat(placeSlots.stream().filter(PlaceSlot::getConfirm).toList().size()).isEqualTo(0),
                () -> assertThat(timeSlots.size()).isEqualTo(0),
                () -> assertThat(participants.size()).isEqualTo(1),
                () -> assertThat(participants.stream()
                        .filter(participant -> participant.getMeetRole().equals(MeetRole.LEADER))
                        .toList().size()).isEqualTo(1)
        );
        verify(scheduler, times(1)).registerAlarm(any(), eq(AlarmType.END_VOTE));
        verify(scheduler, never()).registerAlarm(any(), eq(AlarmType.PROMISE_DAY));
    }

}