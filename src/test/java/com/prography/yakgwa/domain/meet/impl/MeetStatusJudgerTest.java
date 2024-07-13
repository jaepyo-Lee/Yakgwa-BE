package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetStatus;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.prography.yakgwa.domain.meet.entity.MeetStatus.*;
import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class MeetStatusJudgerTest {
    @Autowired
    private MeetJpaRepository meetJpaRepository;
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    private MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    private PlaceSlotJpaRepository placeSlotJpaRepository;
    @Autowired
    private PlaceJpaRepository placeJpaRepository;
    @Autowired
    private PlaceVoteJpaRepository placeVoteJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private MeetStatusJudger meetStatusJudger;
    @Autowired
    private TimeVoteJpaRepository timeVoteJpaRepository;

    @AfterEach
    void init(){
        placeSlotJpaRepository.deleteAll();
        timeVoteJpaRepository.deleteAll();
        timeSlotJpaRepository.deleteAll();
        placeVoteJpaRepository.deleteAll();
        placeJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @BeforeEach
    void initial(){
        placeSlotJpaRepository.deleteAll();
        timeVoteJpaRepository.deleteAll();
        timeSlotJpaRepository.deleteAll();
        placeVoteJpaRepository.deleteAll();
        placeJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @Test
    void 모임이확정상태일때_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme, 24);
        Place place = createAndSavePlace(1L);
        TimeSlot saveTimeSlot = createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), Boolean.TRUE);
        PlaceSlot andSavePlaceSlot = createAndSavePlaceSlot(place, saveMeet, Boolean.TRUE);
        User saveUser = createAndSaveUser(1L);

        // when
        System.out.println("=====Logic Start=====");

        MeetStatus meetStatus = meetStatusJudger.judge(saveMeet, saveUser);

        System.out.println("=====Logic End=====");
        // then
        assertThat(meetStatus).isEqualTo(CONFIRM);
    }

    @Transactional
    @Test
    void 장소와시간투표두개모두에최다득표가있어서_모임의시간이지나서_확정시키고_CONFIRM_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme, 0);
        Place place = createAndSavePlace(1L);
        TimeSlot saveTimeSlot = createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), Boolean.FALSE);
        PlaceSlot andSavePlaceSlot = createAndSavePlaceSlot(place, saveMeet, Boolean.FALSE);
        User saveUser = createAndSaveUser(1L);

        TimeVote andSaveTimeVote = createAndSaveTimeVote(saveTimeSlot, saveUser);
        PlaceVote andSavePlaceVote = createAndSavePlaceVote(saveUser, andSavePlaceSlot);

        // when
        System.out.println("=====Logic Start=====");

        MeetStatus meetStatus = meetStatusJudger.judge(saveMeet, saveUser);

        System.out.println("=====Logic End=====");
        // then
        assertThat(meetStatus).isEqualTo(CONFIRM);
    }

    @Transactional
    @Test
    void 모임의시간이지나서_장소와시간투표두개모두에최다득표가없어서_BEFORE_CONFIRM_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme, 0);
        Place place1 = createAndSavePlace(1L);
        Place place2 = createAndSavePlace(2L);
        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), Boolean.FALSE);
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now().plusDays(1L), Boolean.FALSE);
        PlaceSlot andSavePlaceSlot1 = createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        PlaceSlot andSavePlaceSlot2 = createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        User saveUser1 = createAndSaveUser(1L);
        User saveUser2 = createAndSaveUser(2L);

        TimeVote andSaveTimeVote1 = createAndSaveTimeVote(saveTimeSlot1, saveUser1);
        TimeVote andSaveTimeVote2 = createAndSaveTimeVote(saveTimeSlot2, saveUser2);

        PlaceVote andSavePlaceVote1 = createAndSavePlaceVote(saveUser1, andSavePlaceSlot1);
        PlaceVote andSavePlaceVote2 = createAndSavePlaceVote(saveUser2, andSavePlaceSlot2);

        // when
        System.out.println("=====Logic Start=====");

        MeetStatus meetStatus = meetStatusJudger.judge(saveMeet, saveUser1);

        System.out.println("=====Logic End=====");
        // then
        assertThat(meetStatus).isEqualTo(BEFORE_CONFIRM);
    }

    @Transactional
    @Test
    void 모임의시간이지나기전_확정되지않은상태_하나라도투표를해서_VOTE_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme, 24);
        Place place1 = createAndSavePlace(1L);
        Place place2 = createAndSavePlace(2L);
        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), Boolean.FALSE);
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now().plusDays(1L), Boolean.FALSE);
        PlaceSlot andSavePlaceSlot1 = createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        PlaceSlot andSavePlaceSlot2 = createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        User saveUser1 = createAndSaveUser(1L);
        User saveUser2 = createAndSaveUser(2L);

        PlaceVote andSavePlaceVote1 = createAndSavePlaceVote(saveUser1, andSavePlaceSlot1);

        // when
        System.out.println("=====Logic Start=====");

        MeetStatus meetStatus = meetStatusJudger.judge(saveMeet, saveUser1);

        System.out.println("=====Logic End=====");
        // then
        assertThat(meetStatus).isEqualTo(VOTE);
    }

    @Transactional
    @Test
    void 모임의시간이지나기전_확정되지않은상태_투표하지않아서_BEFORE_VOTE_조회() {
        // given
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1L, theme, 24);
        Place place1 = createAndSavePlace(1L);
        Place place2 = createAndSavePlace(2L);
        TimeSlot saveTimeSlot1 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now(), Boolean.FALSE);
        TimeSlot saveTimeSlot2 = createAndSaveTimeSlot(saveMeet, LocalDateTime.now().plusDays(1L), Boolean.FALSE);
        PlaceSlot andSavePlaceSlot1 = createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        PlaceSlot andSavePlaceSlot2 = createAndSavePlaceSlot(place1, saveMeet, Boolean.FALSE);
        User saveUser1 = createAndSaveUser(1L);
        User saveUser2 = createAndSaveUser(2L);

        // when
        System.out.println("=====Logic Start=====");

        MeetStatus meetStatus = meetStatusJudger.judge(saveMeet, saveUser1);

        System.out.println("=====Logic End=====");
        // then
        assertThat(meetStatus).isEqualTo(BEFORE_VOTE);
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