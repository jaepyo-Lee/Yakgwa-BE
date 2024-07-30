package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.common.DummyCreater;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.service.dto.VoteDateDto;
import com.prography.yakgwa.domain.meet.service.req.MeetCreateRequestDto;
import com.prography.yakgwa.domain.meet.service.req.MeetWithVoteAndStatus;
import com.prography.yakgwa.domain.meet.service.res.MeetInfoWithParticipant;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class MeetServiceInteTest {

    @Autowired
    DummyCreater dummyCreater;

    @Autowired
    MeetService meetService;
    @Autowired
    private MeetJpaRepository meetJpaRepository;
    @Autowired
    private PlaceSlotJpaRepository placeSlotJpaRepository;
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    private ParticipantJpaRepository participantJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @AfterEach
    void init() {
        placeSlotJpaRepository.deleteAll();
        timeSlotJpaRepository.deleteAll();
        participantJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @Test
    void 시간만확정된경우_모임생성() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        String title = "title";
        LocalDateTime confirmTime = LocalDateTime.now();
        String description = "description";
        MeetCreateRequestDto createRequestDto = MeetCreateRequestDto.builder()
                .title(title).meetTime(confirmTime).meetThemeId(saveMeetTheme.getId()).description(description).creatorId(saveUser.getId()).voteDateDto(null).placeInfo(List.of()).confirmPlace(false)
                .build();

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetService.create(createRequestDto);

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
    }

    @Test
    void 장소만확정된경우_모임생성() {
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

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetService.create(createRequestDto);

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
    }

    @Test
    void 시간과장소모두확정된경우_모임생성() {
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

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetService.create(createRequestDto);

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
    }

    @Test
    void 시간과장소모두확정안된경우_모임생성() {
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

        // when
        System.out.println("=====Logic Start=====");

        Meet meet = meetService.create(createRequestDto);

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
    }

    /*====================findWithParticipant====================*/

    @Test
    void 모임의참여원조회() {
        // given
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        User saveUser1 = dummyCreater.createAndSaveUser(1);
        User saveUser2 = dummyCreater.createAndSaveUser(2);
        Participant saveParticipant1 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser1, MeetRole.LEADER);
        Participant saveParticipant2 = dummyCreater.createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);

        // when
        System.out.println("=====Logic Start=====");

        MeetInfoWithParticipant meetInfoWithParticipant = meetService.findWithParticipant(saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(meetInfoWithParticipant.getMeet().getId()).isEqualTo(saveMeet.getId()),
                () -> assertThat(meetInfoWithParticipant.getParticipants().size()).isEqualTo(2));
    }

    /*====================findWithStatus====================*/


    @Test
    void 확정된모임의상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), true);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);


        List<MeetWithVoteAndStatus> meetWithVoteAndStatuses = List.of(MeetWithVoteAndStatus.of(saveMeet, saveTimeSlot, savePlaceSlot, MeetStatus.CONFIRM));
        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(withStatus.size()).isEqualTo(1),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeetStatus).isEqualTo(List.of(MeetStatus.CONFIRM)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeet).isEqualTo(List.of(saveMeet)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getTimeSlot).isEqualTo(List.of(saveTimeSlot)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getPlaceSlot).isEqualTo(List.of(savePlaceSlot)));
    }

    @Test
    void 장소가확정안된투표시간이지나지않은모임의상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), true);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(withStatus.size()).isEqualTo(1),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeetStatus).isEqualTo(List.of(MeetStatus.BEFORE_VOTE)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeet).isEqualTo(List.of(saveMeet)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getTimeSlot).containsExactly(saveTimeSlot),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getPlaceSlot).containsExactly((PlaceSlot) null));
    }

    @Test
    void 시간이확정안된투표시간이지나지않은모임의상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, true);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(withStatus.size()).isEqualTo(1),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeetStatus).isEqualTo(List.of(MeetStatus.BEFORE_VOTE)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeet).isEqualTo(List.of(saveMeet)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getTimeSlot).containsExactly((TimeSlot) null),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getPlaceSlot).containsExactly(savePlaceSlot));
    }

    @Test
    void 모두확정안된투표시간이지나지않은모임의상태와정보_조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        MeetTheme saveMeetTheme = dummyCreater.createAndSaveMeetTheme(1);
        Meet saveMeet = dummyCreater.createAndSaveMeet(1, saveMeetTheme, 24);
        Place savePlace = dummyCreater.createAndSavePlace(1);
        PlaceSlot savePlaceSlot = dummyCreater.createAndSavePlaceSlot(savePlace, saveMeet, false);
        TimeSlot saveTimeSlot = dummyCreater.createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), false);
        dummyCreater.createAndSaveParticipant(saveMeet, saveUser, MeetRole.LEADER);

        // when
        System.out.println("=====Logic Start=====");

        List<MeetWithVoteAndStatus> withStatus = meetService.findWithStatus(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(withStatus.size()).isEqualTo(1),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeetStatus).isEqualTo(List.of(MeetStatus.BEFORE_VOTE)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getMeet).isEqualTo(List.of(saveMeet)),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getTimeSlot).containsExactly((TimeSlot) null),
                () -> assertThat(withStatus).extracting(MeetWithVoteAndStatus::getPlaceSlot).containsExactly((PlaceSlot) null));
    }
    /*=================findPostConfirm=================*/
}