package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.testHelper.config.DeleterConfig;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.meet.entity.Meet;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlaceVoteJpaRepositoryTest extends IntegrationTestSupport {

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 사용자가특정모임에투표했을때확인() {
        // given
        User user = createAndSave(1L);
        User saveUser = userJpaRepository.save(user);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet saveMeet1 = createAndSaveMeet(1L, saveMeetTheme);
        Meet saveMeet2 = createAndSaveMeet(2L, saveMeetTheme);

        Place savePlace = createAndSaveAndSavePlace(1L);

        PlaceSlot savePlaceSlot = createAndSaveAndSavePlaceSlot(savePlace, saveMeet1, false);

        PlaceVote placeVote = createAndSaveAndSavePlaceVote(savePlaceSlot, saveUser);

        // when
        System.out.println("=====Logic Start=====");

        boolean isExist = placeVoteJpaRepository.existsByUserIdAndMeetId(saveUser.getId(), saveMeet1.getId());
        System.out.println(isExist);
        System.out.println("=====Logic End=====");

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    void 사용자가다른모임에만_특정모임에투표했을때_확인() {
        // given
        User user = createAndSave(1L);
        User saveUser = userJpaRepository.save(user);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet saveMeet1 = createAndSaveMeet(1L, saveMeetTheme);
        Meet saveMeet2 = createAndSaveMeet(2L, saveMeetTheme);

        Place savePlace1 = createAndSaveAndSavePlace(1L);
        Place savePlace2 = createAndSaveAndSavePlace(2L);


        PlaceSlot savePlaceSlot3 = createAndSaveAndSavePlaceSlot(savePlace1, saveMeet2, false);
        PlaceSlot savePlaceSlot4 = createAndSaveAndSavePlaceSlot(savePlace2, saveMeet2, false);

        createAndSaveAndSavePlaceVote(savePlaceSlot3, saveUser);
        createAndSaveAndSavePlaceVote(savePlaceSlot4, saveUser);

        // when
        System.out.println("=====Logic Start=====");

        boolean isExist = placeVoteJpaRepository.existsByUserIdAndMeetId(saveUser.getId(), saveMeet1.getId());
        System.out.println(isExist);
        System.out.println("=====Logic End=====");

        // then
        assertThat(isExist).isFalse();
    }


    @Test
    void 사용자의투표가존재하지않을때확인() {
        // given
        User user1 = createAndSave(1L);
        User saveUser1 = userJpaRepository.save(user1);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);

        Meet saveMeet1 = createAndSaveMeet(1L, saveMeetTheme);
        Meet saveMeet2 = createAndSaveMeet(2L, saveMeetTheme);

        Place savePlace2 = createAndSaveAndSavePlace(1L);

        PlaceSlot savePlaceSlot2 = createAndSaveAndSavePlaceSlot(savePlace2, saveMeet2, false);

        PlaceVote placeVote2 = createAndSaveAndSavePlaceVote(savePlaceSlot2, saveUser1);

        // when
        System.out.println("=====Logic Start=====");

        boolean isExist = placeVoteJpaRepository.existsByUserIdAndMeetId(saveUser1.getId(), saveMeet1.getId()); //false
        System.out.println(isExist);

        System.out.println("=====Logic End=====");

        // then
        assertThat(isExist).isFalse();
    }


    @Test
    void 사용자의특정모임에투표한장소_전체조회() {
        // given
        User user = createAndSave(1L);
        User saveUser = userJpaRepository.save(user);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet saveMeet1 = createAndSaveMeet(1L, saveMeetTheme);
        Meet saveMeet2 = createAndSaveMeet(2L, saveMeetTheme);

        Place savePlace1 = createAndSaveAndSavePlace(1L);
        Place savePlace2 = createAndSaveAndSavePlace(2L);

        PlaceSlot savePlaceSlot1 = createAndSaveAndSavePlaceSlot(savePlace1, saveMeet1, false);
        PlaceSlot savePlaceSlot2 = createAndSaveAndSavePlaceSlot(savePlace2, saveMeet1, false);

        PlaceVote placeVote1 = createAndSaveAndSavePlaceVote(savePlaceSlot1, saveUser);

        PlaceVote placeVote2 = createAndSaveAndSavePlaceVote(savePlaceSlot2, saveUser);

        // when
        System.out.println("=====Logic Start=====");

        List<PlaceVote> allByUserIdAndMeetId = placeVoteJpaRepository.findAllByUserIdAndMeetId(saveUser.getId(), saveMeet1.getId());

        System.out.println("=====Logic End=====");
        // then

        assertThat(allByUserIdAndMeetId.size()).isEqualTo(2);

    }

    @Test
    void 다른모임에도투표했을때_사용자의특정모임에투표한장소_전체조회() {
        // given
        User user = createAndSave(1L);
        User saveUser = userJpaRepository.save(user);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet saveMeet1 = createAndSaveMeet(1L, saveMeetTheme);
        Meet saveMeet2 = createAndSaveMeet(2L, saveMeetTheme);

        Place savePlace1 = createAndSaveAndSavePlace(1L);
        Place savePlace2 = createAndSaveAndSavePlace(2L);

        PlaceSlot savePlaceSlot1 = createAndSaveAndSavePlaceSlot(savePlace1, saveMeet1, false);
        PlaceSlot savePlaceSlot2 = createAndSaveAndSavePlaceSlot(savePlace2, saveMeet1, false);

        PlaceSlot savePlaceSlot3 = createAndSaveAndSavePlaceSlot(savePlace1, saveMeet2, false);
        PlaceSlot savePlaceSlot4 = createAndSaveAndSavePlaceSlot(savePlace2, saveMeet2, false);

        createAndSaveAndSavePlaceVote(savePlaceSlot1, saveUser);
        createAndSaveAndSavePlaceVote(savePlaceSlot2, saveUser);
        createAndSaveAndSavePlaceVote(savePlaceSlot3, saveUser);
        createAndSaveAndSavePlaceVote(savePlaceSlot4, saveUser);

        // when
        System.out.println("=====Logic Start=====");

        List<PlaceVote> allByUserIdAndMeetId = placeVoteJpaRepository.findAllByUserIdAndMeetId(saveUser.getId(), saveMeet1.getId());

        System.out.println("=====Logic End=====");
        // then

        assertThat(allByUserIdAndMeetId.size()).isEqualTo(2);

    }

    @Test
    void 사용자가특정모임에투표한내역_삭제() {
        // given
        User saveUser = createAndSave(1L);

        MeetTheme theme = MeetTheme.builder().name("theme1").build();
        MeetTheme saveMeetTheme = meetThemeJpaRepository.save(theme);
        Meet saveMeet1 = createAndSaveMeet(1L, saveMeetTheme);
        Meet saveMeet2 = createAndSaveMeet(2L, saveMeetTheme);

        Place savePlace1 = createAndSaveAndSavePlace(1L);
        Place savePlace2 = createAndSaveAndSavePlace(2L);

        PlaceSlot savePlaceSlot1 = createAndSaveAndSavePlaceSlot(savePlace1, saveMeet1, false);
        PlaceSlot savePlaceSlot2 = createAndSaveAndSavePlaceSlot(savePlace2, saveMeet1, false);

        PlaceSlot savePlaceSlot3 = createAndSaveAndSavePlaceSlot(savePlace1, saveMeet2, false);
        PlaceSlot savePlaceSlot4 = createAndSaveAndSavePlaceSlot(savePlace2, saveMeet2, false);

        createAndSaveAndSavePlaceVote(savePlaceSlot1, saveUser);
        createAndSaveAndSavePlaceVote(savePlaceSlot2, saveUser);
        createAndSaveAndSavePlaceVote(savePlaceSlot3, saveUser);
        createAndSaveAndSavePlaceVote(savePlaceSlot4, saveUser);

        // when
        System.out.println("=====Logic Start=====");

        placeVoteJpaRepository.deleteAllByUserIdAndMeetId(saveUser, saveMeet1.getId());

        System.out.println("=====Logic End=====");
        // then
        List<PlaceVote> allByUserId = placeVoteJpaRepository.findAllByUserId(saveUser.getId());
        assertAll(
                () -> assertThat(allByUserId.size()).isEqualTo(2),
                () -> assertThat(allByUserId.stream()
                        .filter(placeVote -> placeVote.getPlaceSlot().getMeet().getId().equals(saveMeet1.getId()))
                        .toList().size())
                        .isEqualTo(0)
        );

    }


    private Place createAndSaveAndSavePlace(Long id) {
        Place place = PlaceInfoDto.builder()
                .mapx("" + id).mapy("" + id).link("link" + id).address("address" + id).roadAddress("roadAddress" + id).category("category" + id).description("description" + id).title("title" + id).telephone("telephone" + id)
                .build().toEntity();
        return placeJpaRepository.save(place);
    }

    private PlaceVote createAndSaveAndSavePlaceVote(PlaceSlot savePlaceSlot, User saveUser) {
        PlaceVote placeVote = PlaceVote.builder()
                .placeSlot(savePlaceSlot).user(saveUser)
                .build();
        return placeVoteJpaRepository.save(placeVote);

    }

    private PlaceSlot createAndSaveAndSavePlaceSlot(Place savePlace, Meet saveMeet1, boolean confirm) {
        return placeSlotJpaRepository.save(PlaceSlot.builder()
                .place(savePlace).meet(saveMeet1).confirm(confirm)
                .build());
    }

    private Meet createAndSaveMeet(Long id, MeetTheme saveMeetTheme) {
        Meet meet = Meet.builder()
                .title("title" + id).validInviteHour(24).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        return meetJpaRepository.save(meet);
    }

    private User createAndSave(Long id) {
        User user = User.builder()
                .name("user" + id).isNew(true).authId("authId1").authType(KAKAO)
                .build();
        return userJpaRepository.save(user);
    }

}