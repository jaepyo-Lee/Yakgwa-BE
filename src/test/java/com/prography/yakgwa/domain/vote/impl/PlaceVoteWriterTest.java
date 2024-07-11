package com.prography.yakgwa.domain.vote.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
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

    @AfterEach
    void init() {
        placeSlotJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
        placeJpaRepository.deleteAll();
    }

    /*public void confirmAndWrite(Meet meet, boolean isConfirmPlace, List<PlaceInfoDto > placeInfo) {
        java.util.List<Place> placeList = placeInfo.stream()
                .map(placeInfoDto -> placeReader.readByMapxAndMapy(placeInfoDto.getMapx(), placeInfoDto.getMapy())
                        .orElseGet(() -> placeWriter.write(placeInfoDto.toEntity())))
                .toList();

        placeList.forEach(place ->
                placeSlotWriter.write(PlaceSlot.builder().meet(meet).confirm(isConfirmPlace).place(place).build()));
    }*/

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

        // when
        System.out.println("=====Logic Start=====");

        placeVoteWriter.confirmAndWrite(saveMeet, false, List.of(placeInfoDto1, placeInfoDto2));

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

        // when
        System.out.println("=====Logic Start=====");

        placeVoteWriter.confirmAndWrite(saveMeet, false, List.of(placeInfoDto));

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

        // when
        System.out.println("=====Logic Start=====");

        placeVoteWriter.confirmAndWrite(saveMeet, true, List.of(placeInfoDto1));

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

        // when
        System.out.println("=====Logic Start=====");

        placeVoteWriter.confirmAndWrite(saveMeet, true, List.of(placeInfoDto1));

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

        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThrows(RuntimeException.class, () -> placeVoteWriter.confirmAndWrite(saveMeet, true, List.of(placeInfoDto1, placeInfoDto2)));

        System.out.println("=====Logic End=====");
    }
}