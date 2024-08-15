package com.prography.yakgwa.domain.meet.controller;

import com.prography.yakgwa.domain.common.ControllerUnitTestEnvironment;
import com.prography.yakgwa.domain.meet.controller.req.CreateMeetRequest;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.testHelper.mock.WithCustomMockUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class MeetCreateControllerTest extends ControllerUnitTestEnvironment {



    @WithCustomMockUser
    @Test
    void 모임생성컨트롤러테스트() throws Exception {
        // given
        String dateString = "2024-08-03 15:30:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        CreateMeetRequest build = CreateMeetRequest.builder()
                .meetInfo(CreateMeetRequest.MeetInfo.builder()
                        .meetTitle("title")
                        .meetThemeId(1L)
                        .meetTime(dateTime)
                        .confirmPlace(true)
                        .placeInfo(List.of(
                                PlaceInfoDto.builder()
                                        .title("title").mapy("mapy").mapx("mapx").link("link").roadAddress("roadAddress").category("category").address("address").telephone("telephone").description("description")
                                        .build())
                        )
                        .description("description")
                        .voteDate(null)
                        .build())
                .build();
        Meet meet = Meet.builder()
                .id(1L)
                .build();
        when(meetCreateService.create(any())).thenReturn(meet);

        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets")
                        .content(objectMapper.writeValueAsString(build))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken")  // Adjust this if needed
                )
                .andDo(print())
                .andExpect(jsonPath("$.result.meetId").value(meet.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println("=====Logic End=====");
        // then
    }

    @WithCustomMockUser
    @Test
    void 모임제목없을떄유효성테스트() throws Exception {
        // given
        String dateString = "2024-08-03 15:30:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        CreateMeetRequest build = CreateMeetRequest.builder()
                .meetInfo(CreateMeetRequest.MeetInfo.builder()
                        .meetTitle("")
                        .meetThemeId(1L)
                        .meetTime(dateTime)
                        .confirmPlace(true)
                        .placeInfo(List.of(
                                PlaceInfoDto.builder()
                                        .title("title").mapy("mapy").mapx("mapx").link("link").roadAddress("roadAddress").category("category").address("address").telephone("telephone").description("description")
                                        .build())
                        )
                        .description("description")
                        .voteDate(null)
                        .build())
                .build();
        Meet meet = Meet.builder()
                .id(1L)
                .build();
        when(meetCreateService.create(any())).thenReturn(meet);

        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets")
                        .content(objectMapper.writeValueAsString(build))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken")  // Adjust this if needed
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("=====Logic End=====");
        // then
    }

    @WithCustomMockUser
    @Test
    void 모임제목null일때유효성테스트() throws Exception {
        // given
        String dateString = "2024-08-03 15:30:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        CreateMeetRequest build = CreateMeetRequest.builder()
                .meetInfo(CreateMeetRequest.MeetInfo.builder()
                        .meetTitle(null)
                        .meetThemeId(1L)
                        .meetTime(dateTime)
                        .confirmPlace(true)
                        .placeInfo(List.of(
                                PlaceInfoDto.builder()
                                        .title("title").mapy("mapy").mapx("mapx").link("link").roadAddress("roadAddress").category("category").address("address").telephone("telephone").description("description")
                                        .build())
                        )
                        .description("description")
                        .voteDate(null)
                        .build())
                .build();
        Meet meet = Meet.builder()
                .id(1L)
                .build();
        when(meetCreateService.create(any())).thenReturn(meet);

        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets")
                        .content(objectMapper.writeValueAsString(build))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken")  // Adjust this if needed
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("=====Logic End=====");
        // then
    }

    @WithCustomMockUser
    @Test
    void 모임테마null일때유효성테스트() throws Exception {
        // given
        String dateString = "2024-08-03 15:30:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        CreateMeetRequest build = CreateMeetRequest.builder()
                .meetInfo(CreateMeetRequest.MeetInfo.builder()
                        .meetTitle("title")
                        .meetThemeId(null)
                        .meetTime(dateTime)
                        .confirmPlace(true)
                        .placeInfo(List.of(
                                PlaceInfoDto.builder()
                                        .title("title").mapy("mapy").mapx("mapx").link("link").roadAddress("roadAddress").category("category").address("address").telephone("telephone").description("description")
                                        .build())
                        )
                        .description("description")
                        .voteDate(null)
                        .build())
                .build();
        Meet meet = Meet.builder()
                .id(1L)
                .build();
        when(meetCreateService.create(any())).thenReturn(meet);

        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets")
                        .content(objectMapper.writeValueAsString(build))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken")  // Adjust this if needed
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("=====Logic End=====");
        // then
    }

    @WithCustomMockUser
    @Test
    void 모임장소확정여부가null일때유효성테스트() throws Exception {
        // given
        String dateString = "2024-08-03 15:30:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        CreateMeetRequest build = CreateMeetRequest.builder()
                .meetInfo(CreateMeetRequest.MeetInfo.builder()
                        .meetTitle("title")
                        .meetThemeId(1L)
                        .meetTime(dateTime)
                        .confirmPlace(null)
                        .placeInfo(List.of(
                                PlaceInfoDto.builder()
                                        .title("title").mapy("mapy").mapx("mapx").link("link").roadAddress("roadAddress").category("category").address("address").telephone("telephone").description("description")
                                        .build())
                        )
                        .description("description")
                        .voteDate(null)
                        .build())
                .build();
        Meet meet = Meet.builder()
                .id(1L)
                .build();
        when(meetCreateService.create(any())).thenReturn(meet);

        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets")
                        .content(objectMapper.writeValueAsString(build))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken")  // Adjust this if needed
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("=====Logic End=====");
        // then
    }

    /**
     * Todo
     * Work) 날짜 포맷 유효성 Test Code
     * Write-Date) 2024-08-13, 화, 15:0
     * Finish-Date)
     */
  /*  @WithCustomMockUser
    @Test
    void 날짜포맷유효성테스트() throws Exception {
        // given
        CreateMeetRequest build = CreateMeetRequest.builder()
                .meetInfo(CreateMeetRequest.MeetInfo.builder()
                        .meetTitle("title")
                        .meetThemeId(1L)
                        .meetTime(LocalDateTime.parse("2022-12-01T12:12:12"))
                        .confirmPlace(true)
                        .placeInfo(List.of(
                                PlaceInfoDto.builder()
                                        .title("title").mapy("mapy").mapx("mapx").link("link").roadAddress("roadAddress").category("category").address("address").telephone("telephone").description("description")
                                        .build())
                        )
                        .description("description")
                        .voteDate(null)
                        .build())
                .build();
        Meet meet = Meet.builder()
                .id(1L)
                .build();
        when(meetService.create(any())).thenReturn(meet);

        // when
        System.out.println("=====Logic Start=====");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets")
                        .content(objectMapper.writeValueAsString(build))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken")  // Adjust this if needed
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        System.out.println("=====Logic End=====");
        // then
    }*/
}