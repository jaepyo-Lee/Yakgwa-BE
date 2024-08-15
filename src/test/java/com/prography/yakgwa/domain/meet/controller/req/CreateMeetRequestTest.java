package com.prography.yakgwa.domain.meet.controller.req;

import com.prography.yakgwa.domain.meet.service.req.MeetCreateRequestDto;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.global.format.exception.param.TimeParamException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateMeetRequestTest {
    @Test
    void 시간이확정되었을떄RequestDto로변환테스트() {
        // given
        LocalDateTime time = LocalDateTime.now();
        String title = "title";
        long meetThemeId = 1L;
        String description = "description";
        String placedescription = "placedescription";
        String placetitle = "placetitle";
        String placetelephone = "placetelephone";
        String placemapx = "placemapx";
        String placemapy = "placemapy";
        String placecategory = "placecategory";
        String placeroadaddress = "placeroadaddress";

        CreateMeetRequest request = CreateMeetRequest.builder()
                .meetInfo(CreateMeetRequest.MeetInfo.builder()
                        .meetTitle(title)
                        .meetThemeId(meetThemeId)
                        .meetTime(time)
                        .description(description)
                        .placeInfo(List.of(PlaceInfoDto.builder()
                                .description(placedescription).title(placetitle).telephone(placetelephone).mapx(placemapx).mapy(placemapy).category(placecategory).roadAddress(placeroadaddress)
                                .build()))
                        .confirmPlace(false)
                        .build())
                .build();
        long creatorId = 1L;

        // when
        System.out.println("=====Logic Start=====");

        MeetCreateRequestDto requestDto = request.toRequestDto(creatorId);

        System.out.println("=====Logic End=====");
        // then
        assertAll(
                () -> assertThat(requestDto.getCreatorId()).isEqualTo(creatorId),
                () -> assertThat(requestDto.getMeetTime()).isEqualTo(time),
                () -> assertThat(requestDto.getTitle()).isEqualTo(title),
                () -> assertThat(requestDto.getVoteDateDto()).isNull(),
                () -> assertThat(requestDto.getMeetThemeId()).isEqualTo(meetThemeId),
                () -> assertThat(requestDto.getPlaceInfo().size()).isEqualTo(1)
        );
    }

    @Test
    void 시간이확정안되었을떄RequestDto로변환테스트() {
        // given
        String title = "title";
        long meetThemeId = 1L;
        String description = "description";
        String placedescription = "placedescription";
        String placetitle = "placetitle";
        String placetelephone = "placetelephone";
        String placemapx = "placemapx";
        String placemapy = "placemapy";
        String placecategory = "placecategory";
        String placeroadaddress = "placeroadaddress";

        LocalDate startVoteDate = LocalDate.now().minusDays(1L);
        LocalDate endVoteDate = LocalDate.now().plusDays(1L);
        CreateMeetRequest request = CreateMeetRequest.builder()
                .meetInfo(CreateMeetRequest.MeetInfo.builder()
                        .meetTitle(title)
                        .voteDate(CreateMeetRequest.VoteDate.builder()
                                .startVoteDate(startVoteDate)
                                .endVoteDate(endVoteDate)
                                .build())
                        .meetThemeId(meetThemeId)
                        .description(description)
                        .placeInfo(List.of(PlaceInfoDto.builder()
                                .description(placedescription)
                                .title(placetitle)
                                .telephone(placetelephone)
                                .mapx(placemapx)
                                .mapy(placemapy)
                                .category(placecategory)
                                .roadAddress(placeroadaddress)
                                .build()))
                        .confirmPlace(false)
                        .build())
                .build();
        long creatorId = 1L;

        // when
        System.out.println("=====Logic Start=====");

        MeetCreateRequestDto requestDto = request.toRequestDto(creatorId);

        System.out.println("=====Logic End=====");
        // then
        assertAll(
                () -> assertThat(requestDto.getCreatorId()).isEqualTo(creatorId),
                () -> assertThat(requestDto.getMeetTime()).isNull(),
                () -> assertThat(requestDto.getVoteDateDto().getStartVoteDate()).isEqualTo(startVoteDate),
                () -> assertThat(requestDto.getVoteDateDto().getEndVoteDate()).isEqualTo(endVoteDate),
                () -> assertThat(requestDto.getTitle()).isEqualTo(title),
                () -> assertThat(requestDto.getMeetThemeId()).isEqualTo(meetThemeId),
                () -> assertThat(requestDto.getPlaceInfo().size()).isEqualTo(1)
        );
    }

    @Test
    void 시간확정값과투표기간이함께들어왔을떄RequestDto로변환테스트() {
        // given
        String title = "title";
        long meetThemeId = 1L;
        String description = "description";
        String placedescription = "placedescription";
        String placetitle = "placetitle";
        String placetelephone = "placetelephone";
        String placemapx = "placemapx";
        String placemapy = "placemapy";
        String placecategory = "placecategory";
        String placeroadaddress = "placeroadaddress";

        LocalDate startVoteDate = LocalDate.now().minusDays(1L);
        LocalDate endVoteDate = LocalDate.now().plusDays(1L);
        LocalDateTime time = LocalDateTime.now();
        CreateMeetRequest request = CreateMeetRequest.builder()
                .meetInfo(CreateMeetRequest.MeetInfo.builder()
                        .meetTitle(title)
                        .voteDate(CreateMeetRequest.VoteDate.builder()
                                .startVoteDate(startVoteDate)
                                .endVoteDate(endVoteDate)
                                .build())
                        .meetTime(time)
                        .meetThemeId(meetThemeId)
                        .description(description)
                        .placeInfo(List.of(PlaceInfoDto.builder()
                                .description(placedescription)
                                .title(placetitle)
                                .telephone(placetelephone)
                                .mapx(placemapx)
                                .mapy(placemapy)
                                .category(placecategory)
                                .roadAddress(placeroadaddress)
                                .build()))
                        .confirmPlace(false)
                        .build())
                .build();
        long creatorId = 1L;

        // when
        System.out.println("=====Logic Start=====");

        assertThrows(TimeParamException.class, () -> request.toRequestDto(creatorId));

        System.out.println("=====Logic End=====");
        // then
    }


    @Test
    void 시간확정값과투표기간이모두없을떄RequestDto로변환테스트() {
        // given
        String title = "title";
        long meetThemeId = 1L;
        String description = "description";
        String placedescription = "placedescription";
        String placetitle = "placetitle";
        String placetelephone = "placetelephone";
        String placemapx = "placemapx";
        String placemapy = "placemapy";
        String placecategory = "placecategory";
        String placeroadaddress = "placeroadaddress";

        CreateMeetRequest request = CreateMeetRequest.builder()
                .meetInfo(CreateMeetRequest.MeetInfo.builder()
                        .meetTitle(title)
                        .meetThemeId(meetThemeId)
                        .description(description)
                        .placeInfo(List.of(PlaceInfoDto.builder()
                                .description(placedescription)
                                .title(placetitle)
                                .telephone(placetelephone)
                                .mapx(placemapx)
                                .mapy(placemapy)
                                .category(placecategory)
                                .roadAddress(placeroadaddress)
                                .build()))
                        .confirmPlace(false)
                        .build())
                .build();
        long creatorId = 1L;

        // when
        System.out.println("=====Logic Start=====");

        assertThrows(TimeParamException.class, () -> request.toRequestDto(creatorId));

        System.out.println("=====Logic End=====");
        // then
    }
}