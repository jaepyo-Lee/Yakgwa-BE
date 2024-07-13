package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.impl.dto.ConfirmPlaceDto;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class PlaceVoteWriterTest {
    @Autowired
    PlaceJpaRepository placeJpaRepository;
    @Autowired
    PlaceSlotJpaRepository placeSlotJpaRepository;
    @Autowired
    MeetJpaRepository meetJpaRepository;
    @Autowired
    MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    PlaceVoteWriter voteWriter;
    @Autowired
    private PlaceVoteWriter placeVoteWriter;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PlaceVoteJpaRepository placeVoteJpaRepository;

    @AfterEach
    void init() {
        placeVoteJpaRepository.deleteAll();
        placeSlotJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
        placeJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @Test
    void 장소가존재하지않을때저장하고_확정되지않은장소후보_등록() {
        // given
        MeetTheme theme = MeetTheme.builder()
                .name("데이트")
                .build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);

        Meet meet = Meet.builder()
                .title("title").period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).validInviteHour(24).meetTheme(saveTheme)
                .build();
        Meet saveMeet = meetJpaRepository.save(meet);

        PlaceInfoDto placeInfoDto1 = PlaceInfoDto.builder()
                .mapx("321").mapy("123").link("link1").address("address1").roadAddress("roadAddress1").category("category1").description("description1").title("title1").telephone("telephone1")
                .build();

        PlaceInfoDto placeInfoDto2 = PlaceInfoDto.builder()
                .mapx("1232").mapy("3212").link("link2").address("address2").roadAddress("roadAddress2").category("category2").description("description2").title("title2").telephone("telephone2")
                .build();
        ConfirmPlaceDto confirmPlaceDto = ConfirmPlaceDto.builder().confirmPlace(false).placeInfo(List.of(placeInfoDto1, placeInfoDto2)).build();
        // when
        System.out.println("=====Logic Start=====");

        placeVoteWriter.decideConfirmAndWrite(saveMeet, confirmPlaceDto);

        System.out.println("=====Logic End=====");
        // then
        List<Place> places = placeJpaRepository.findAll();
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meet.getId());
        assertAll(() -> assertThat(places.size()).isEqualTo(2),
                () -> assertThat(placeSlots.size()).isEqualTo(2),
                () -> assertEquals(2, placeSlots.stream()
                        .filter(placeSlot -> placeSlot.getConfirm().equals(Boolean.FALSE))
                        .toList()
                        .size()));
    }

    @Test
    void 장소가존재할때_확정되지않은장소후보_등록() {
        // given
        MeetTheme theme = MeetTheme.builder()
                .name("데이트")
                .build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);

        Meet meet = Meet.builder()
                .title("title").period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).validInviteHour(24).meetTheme(saveTheme)
                .build();
        Meet saveMeet = meetJpaRepository.save(meet);

        PlaceInfoDto placeInfoDto = PlaceInfoDto.builder()
                .mapx("321").mapy("123").link("link1").address("address1").roadAddress("roadAddress1").category("category1").description("description1").title("title1").telephone("telephone1")
                .build();

        placeJpaRepository.save(placeInfoDto.toEntity());
        ConfirmPlaceDto confirmPlaceDto = ConfirmPlaceDto.builder().confirmPlace(false).placeInfo(List.of(placeInfoDto)).build();

        // when
        System.out.println("=====Logic Start=====");

        placeVoteWriter.decideConfirmAndWrite(saveMeet, confirmPlaceDto);

        System.out.println("=====Logic End=====");
        // then
        List<Place> places = placeJpaRepository.findAll();
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meet.getId());
        assertAll(() -> assertThat(places.size()).isEqualTo(1),
                () -> assertThat(placeSlots.size()).isEqualTo(1),
                () -> assertEquals(1, placeSlots.stream()
                        .filter(placeSlot -> placeSlot.getConfirm().equals(Boolean.FALSE))
                        .toList()
                        .size()));

    }

    @Test
    void 장소가존재하지않을때저장하고_확정된장소후보_등록() {
        // given
        MeetTheme theme = MeetTheme.builder()
                .name("데이트")
                .build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);

        Meet meet = Meet.builder()
                .title("title").period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).validInviteHour(24).meetTheme(saveTheme)
                .build();
        Meet saveMeet = meetJpaRepository.save(meet);

        PlaceInfoDto placeInfoDto1 = PlaceInfoDto.builder()
                .mapx("321").mapy("123").link("link1").address("address1").roadAddress("roadAddress1").category("category1").description("description1").title("title1").telephone("telephone1")
                .build();
        ConfirmPlaceDto confirmPlaceDto = ConfirmPlaceDto.builder().confirmPlace(true).placeInfo(List.of(placeInfoDto1)).build();

        // when
        System.out.println("=====Logic Start=====");

        placeVoteWriter.decideConfirmAndWrite(saveMeet, confirmPlaceDto);

        System.out.println("=====Logic End=====");
        // then
        List<Place> places = placeJpaRepository.findAll();
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meet.getId());
        assertAll(() -> assertThat(places.size()).isEqualTo(1),
                () -> assertThat(placeSlots.size()).isEqualTo(1),
                () -> assertEquals(1, placeSlots.stream()
                        .filter(placeSlot -> placeSlot.getConfirm().equals(Boolean.TRUE))
                        .toList()
                        .size()),
                () -> assertEquals(0, placeSlots.stream()
                        .filter(placeSlot -> placeSlot.getConfirm().equals(Boolean.FALSE))
                        .toList()
                        .size()));

    }

    @Test
    void 장소가존재할때_확정된장소후보_등록() {
        // given
        MeetTheme theme = MeetTheme.builder()
                .name("데이트")
                .build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);

        Meet meet = Meet.builder()
                .title("title").period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).validInviteHour(24).meetTheme(saveTheme)
                .build();
        Meet saveMeet = meetJpaRepository.save(meet);

        PlaceInfoDto placeInfoDto1 = PlaceInfoDto.builder()
                .mapx("321").mapy("123").link("link1").address("address1").roadAddress("roadAddress1").category("category1").description("description1").title("title1").telephone("telephone1")
                .build();

        placeJpaRepository.save(placeInfoDto1.toEntity());
        ConfirmPlaceDto confirmPlaceDto = ConfirmPlaceDto.builder().confirmPlace(true).placeInfo(List.of(placeInfoDto1)).build();

        // when
        System.out.println("=====Logic Start=====");

        placeVoteWriter.decideConfirmAndWrite(saveMeet, confirmPlaceDto);

        System.out.println("=====Logic End=====");
        // then
        List<Place> places = placeJpaRepository.findAll();
        List<PlaceSlot> placeSlots = placeSlotJpaRepository.findAllByMeetId(meet.getId());
        assertAll(() -> assertThat(places.size()).isEqualTo(1),
                () -> assertThat(placeSlots.size()).isEqualTo(1),
                () -> assertEquals(1, placeSlots.stream()
                        .filter(placeSlot -> placeSlot.getConfirm().equals(Boolean.TRUE))
                        .toList()
                        .size()),
                () -> assertEquals(0, placeSlots.stream()
                        .filter(placeSlot -> placeSlot.getConfirm().equals(Boolean.FALSE))
                        .toList()
                        .size()));

    }

    @Test
    void 확정된장소를등록할때_확정된한곳이아닌여러장소가들어왔을때_예외() {
        // given
        MeetTheme theme = MeetTheme.builder()
                .name("데이트")
                .build();
        MeetTheme saveTheme = meetThemeJpaRepository.save(theme);

        Meet meet = Meet.builder()
                .title("title").period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).validInviteHour(24).meetTheme(saveTheme)
                .build();
        Meet saveMeet = meetJpaRepository.save(meet);

        PlaceInfoDto placeInfoDto1 = PlaceInfoDto.builder()
                .mapx("321").mapy("123").link("link1").address("address1").roadAddress("roadAddress1").category("category1").description("description1").title("title1").telephone("telephone1")
                .build();

        PlaceInfoDto placeInfoDto2 = PlaceInfoDto.builder()
                .mapx("321").mapy("123").link("link1").address("address1").roadAddress("roadAddress1").category("category1").description("description1").title("title1").telephone("telephone1")
                .build();
        ConfirmPlaceDto confirmPlaceDto = ConfirmPlaceDto.builder().confirmPlace(true).placeInfo(List.of(placeInfoDto1, placeInfoDto2)).build();

        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThrows(RuntimeException.class, () -> placeVoteWriter.decideConfirmAndWrite(saveMeet, confirmPlaceDto));

        System.out.println("=====Logic End=====");
    }

    @Test
    void 장소투표_전체저장() {
        // given
        User saveUser = createAndSaveUser(1L);
        MeetTheme theme = meetThemeJpaRepository.save(MeetTheme.builder().name("theme").build());
        Meet saveMeet = createAndSaveMeet(1, theme);
        Place place1 = createAndSavePlace(1);
        Place place2 = createAndSavePlace(2);
        Place place3 = createAndSavePlace(3);

        PlaceSlot placeSlot1 = createAndSave(place1, saveMeet, false);
        PlaceSlot placeSlot2 = createAndSave(place2, saveMeet, false);
        PlaceSlot placeSlot3 = createAndSave(place3, saveMeet, false);

        // when
        System.out.println("=====Logic Start=====");

        placeVoteWriter.writeAll(saveUser, List.of(placeSlot1, placeSlot2, placeSlot3));

        System.out.println("=====Logic End=====");
        // then
        List<PlaceSlot> all = placeSlotJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(3);

    }

    private PlaceSlot createAndSave(Place place, Meet saveMeet, boolean confirm) {
        return placeSlotJpaRepository.save(PlaceSlot.builder().place(place).confirm(confirm).meet(saveMeet).build());
    }

    private Place createAndSavePlace(int idx) {
        Place place = Place.builder()
                .mapy("" + idx).mapx("" + idx).link("link" + idx).title("title" + idx).roadAddress("roadAddress" + idx).address("address" + idx).description("description" + idx).category("category" + idx).telephone("telephone" + idx)
                .build();
        return placeJpaRepository.save(place);
    }

    private User createAndSaveUser(Long id) {
        User user = User.builder()
                .name("name" + id).imageUrl("imageUrl" + id).fcmToken("fcmtoken" + id).authId("authId" + id).authType(AuthType.KAKAO).isNew(true)
                .build();
        return userJpaRepository.save(user);
    }

    private Meet createAndSaveMeet(int idx, MeetTheme saveTheme) {
        Meet meet = Meet.builder()
                .title("title" + idx).validInviteHour(24).meetTheme(saveTheme).period(new VotePeriod(now(), now().plusDays(1L)))
                .build();
        return meetJpaRepository.save(meet);
    }
}