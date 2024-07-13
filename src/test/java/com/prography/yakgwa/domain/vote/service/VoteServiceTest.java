package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.enumerate.VoteStatus;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import com.prography.yakgwa.domain.vote.service.req.PlaceInfosByMeetStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.prography.yakgwa.domain.meet.entity.MeetStatus.BEFORE_VOTE;
import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class VoteServiceTest {

    @Autowired
    private MeetJpaRepository meetJpaRepository;
    @Autowired
    private PlaceJpaRepository placeJpaRepository;
    @Autowired
    private PlaceVoteJpaRepository placeVoteJpaRepository;
    @Autowired
    private PlaceSlotJpaRepository placeSlotJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private TimeVoteJpaRepository timeVoteJpaRepository;
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    private MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    private ParticipantJpaRepository participantJpaRepository;
    @Autowired
    private VoteService voteService;

    @Test
    void 약과원_모임확정되지않았을때_투표안했을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme, 24);
        User saveUser = createAndSaveUser(1L);
        Participant saveParticipant = createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = createAndSavePlace(1L);
        PlaceSlot savePlaceSlot = createAndSavePlaceSlot(place, saveMeet, false);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(()-> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_VOTE),
                ()-> assertThat(placeInfoWithMeetStatus.getPlaces()).isEmpty());

    }

    @Test
    void 약과원_모임확정되지않았을때_투표했을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme, 24);
        User saveUser = createAndSaveUser(1L);
        Participant saveParticipant = createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = createAndSavePlace(1L);
        PlaceSlot savePlaceSlot = createAndSavePlaceSlot(place, saveMeet, false);
        createAndSavePlaceVote(saveUser, savePlaceSlot);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(()-> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.VOTE),
                ()-> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(1));

    }

    @Test
    void 약과장_모임장소확정안되었을때_투표시간지났을때_최다득표가있을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme, 0);
        User saveUser1 = createAndSaveUser(1L);
        User saveUser2 = createAndSaveUser(1L);
        Participant saveParticipant1 = createAndSaveParticipant(saveMeet, saveUser1, MeetRole.LEADER);
        Participant saveParticipant2 = createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);

        Place place1 = createAndSavePlace(1L);
        Place place2 = createAndSavePlace(2L);
        Place place3 = createAndSavePlace(3L);
        PlaceSlot savePlaceSlot1 = createAndSavePlaceSlot(place1, saveMeet, false);
        PlaceSlot savePlaceSlot2 = createAndSavePlaceSlot(place2, saveMeet, false);
        PlaceSlot savePlaceSlot3 = createAndSavePlaceSlot(place3, saveMeet, false);

        createAndSavePlaceVote(saveUser1, savePlaceSlot1);
        createAndSavePlaceVote(saveUser1, savePlaceSlot2);
        createAndSavePlaceVote(saveUser1, savePlaceSlot3);

        createAndSavePlaceVote(saveUser2, savePlaceSlot1);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser1.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(()-> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.CONFIRM),
                ()-> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(1));

    }

    @Test
    void 약과장_모임장소확정안되었을때_투표시간지났을때_동표일때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme, 0);
        User saveUser1 = createAndSaveUser(1L);
        User saveUser2 = createAndSaveUser(1L);
        Participant saveParticipant1 = createAndSaveParticipant(saveMeet, saveUser1, MeetRole.LEADER);
        Participant saveParticipant2 = createAndSaveParticipant(saveMeet, saveUser2, MeetRole.PARTICIPANT);

        Place place1 = createAndSavePlace(1L);
        Place place2 = createAndSavePlace(2L);
        Place place3 = createAndSavePlace(3L);
        PlaceSlot savePlaceSlot1 = createAndSavePlaceSlot(place1, saveMeet, false);
        PlaceSlot savePlaceSlot2 = createAndSavePlaceSlot(place2, saveMeet, false);
        PlaceSlot savePlaceSlot3 = createAndSavePlaceSlot(place3, saveMeet, false);

        createAndSavePlaceVote(saveUser1, savePlaceSlot1);
        createAndSavePlaceVote(saveUser1, savePlaceSlot2);
        createAndSavePlaceVote(saveUser1, savePlaceSlot3);

        createAndSavePlaceVote(saveUser2, savePlaceSlot1);
        createAndSavePlaceVote(saveUser2, savePlaceSlot2);
        createAndSavePlaceVote(saveUser2, savePlaceSlot3);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser1.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(()-> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.BEFORE_CONFIRM),
                ()-> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(3));

    }

    @Test
    void 약과원_모임장소확정되었을때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme, 24);
        User saveUser = createAndSaveUser(1L);
        Participant saveParticipant = createAndSaveParticipant(saveMeet, saveUser, MeetRole.PARTICIPANT);

        Place place = createAndSavePlace(1L);
        PlaceSlot savePlaceSlot = createAndSavePlaceSlot(place, saveMeet, true);

        // when
        System.out.println("=====Logic Start=====");

        PlaceInfosByMeetStatus placeInfoWithMeetStatus = voteService.findPlaceInfoWithMeetStatus(saveUser.getId(), saveMeet.getId());

        System.out.println("=====Logic End=====");
        // then

        assertAll(()-> assertThat(placeInfoWithMeetStatus.getVoteStatus()).isEqualTo(VoteStatus.CONFIRM),
                ()-> assertThat(placeInfoWithMeetStatus.getPlaces().size()).isEqualTo(1));

    }

    private Participant createAndSaveParticipant(Meet saveMeet, User saveUser, MeetRole meetRole) {
        Participant participant = Participant.builder().meet(saveMeet).user(saveUser).meetRole(meetRole).build();
        return participantJpaRepository.save(participant);
    }

    private PlaceVote createAndSavePlaceVote(User saveUser, PlaceSlot andSavePlaceSlot) {
        PlaceVote placeVote = PlaceVote.builder().user(saveUser).placeSlot(andSavePlaceSlot).build();
        return placeVoteJpaRepository.save(placeVote);
    }

    private TimeVote createAndSaveTimeVote(TimeSlot saveTimeSlot, User saveUser) {
        TimeVote timeVote = TimeVote.builder().timeSlot(saveTimeSlot).user(saveUser).build();
        return timeVoteJpaRepository.save(timeVote);
    }

    private TimeSlot createAndSaveTimeSlot(Meet saveMeet, LocalDateTime time, boolean confirm) {
        return timeSlotJpaRepository.save(TimeSlot.builder().meet(saveMeet).time(time).confirm(confirm).build());
    }

    private Meet createAndSaveMeet(Long id, MeetTheme saveMeetTheme, int validInviteHour) {
        Meet meet = Meet.builder()
                .title("title" + id).validInviteHour(validInviteHour).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        return meetJpaRepository.save(meet);
    }
    private Place createAndSavePlace(Long id) {
        Place place = PlaceInfoDto.builder()
                .mapx("" + id).mapy("" + id).link("link" + id).address("address" + id).roadAddress("roadAddress" + id).category("category" + id).description("description" + id).title("title" + id).telephone("telephone" + id)
                .build().toEntity();
        return placeJpaRepository.save(place);
    }

    private PlaceVote createAndSavePlaceVote(PlaceSlot savePlaceSlot, User saveUser) {
        PlaceVote placeVote = PlaceVote.builder()
                .placeSlot(savePlaceSlot).user(saveUser)
                .build();
        return placeVoteJpaRepository.save(placeVote);

    }

    private PlaceSlot createAndSavePlaceSlot(Place savePlace, Meet saveMeet1, boolean confirm) {
        return placeSlotJpaRepository.save(PlaceSlot.builder()
                .place(savePlace).meet(saveMeet1).confirm(confirm)
                .build());
    }


    private User createAndSaveUser(Long id) {
        User user = User.builder()
                .name("user" + id).isNew(true).authId("authId1").authType(KAKAO)
                .build();
        return userJpaRepository.save(user);
    }
}