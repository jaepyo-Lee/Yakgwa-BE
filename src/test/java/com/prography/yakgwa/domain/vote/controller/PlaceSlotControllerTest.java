package com.prography.yakgwa.domain.vote.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.prography.yakgwa.domain.common.ControllerUnitTestEnvironment;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.controller.req.PlaceSlotAppendRequest;
import com.prography.yakgwa.domain.vote.controller.res.AllPlaceSlotOfMeetResponse;
import com.prography.yakgwa.domain.vote.controller.res.NewPlaceSlotResponse;
import com.prography.yakgwa.domain.vote.controller.res.PlaceSlotOfMeet;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.service.place.res.PlaceSlotWithUserResponse;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PlaceSlotControllerTest extends ControllerUnitTestEnvironment {

    @Test
    void 모임의후보장소조회() throws Exception {
        // given
        Long meetId = 1L;
        PlaceSlot mockPlaceSlot = mock(PlaceSlot.class);
        Place mockPlace = mock(Place.class);
        User mockUser = mock(User.class);

        when(mockPlaceSlot.getPlace()).thenReturn(mockPlace);
        when(mockPlaceSlot.getId()).thenReturn(1L);
        when(mockPlace.getTitle()).thenReturn("Test Place");
        when(mockPlace.getAddress()).thenReturn("Test Address");
        when(mockUser.getName()).thenReturn("Test User");
        when(mockUser.getImageUrl()).thenReturn("test_image_url");
        List<User> users = List.of(mockUser);
        PlaceSlotWithUserResponse placeSlotWithUserResponse = PlaceSlotWithUserResponse.of(mockPlaceSlot, users);
        List<PlaceSlotWithUserResponse> slotInMeet = List.of(placeSlotWithUserResponse);

        when(placeSlotService.findPlaceSlotFrom(meetId)).thenReturn(slotInMeet);
        // when
        MvcResult mvcResult = mvc.perform(get("/api/v1/meets/{meetId}/placeslots", meetId)
                        .header("Authorization", "Bearer validToken")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        SuccessResponse<AllPlaceSlotOfMeetResponse> response = objectMapper.readValue(contentAsString, new TypeReference<SuccessResponse<AllPlaceSlotOfMeetResponse>>() {});
        AllPlaceSlotOfMeetResponse result = response.getResult();

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getPlaceSlotOfMeet()).hasSize(1),
                () -> assertThat(result.getPlaceSlotOfMeet().get(0).getPlaceSlotId()).isEqualTo(1L),
                () -> assertThat(result.getPlaceSlotOfMeet().get(0).getPlaceName()).isEqualTo("Test Place"),
                () -> assertThat(result.getPlaceSlotOfMeet().get(0).getPlaceAddress()).isEqualTo("Test Address"),
                () -> assertThat(result.getPlaceSlotOfMeet().get(0).getUserInfos()).hasSize(1),
                () -> assertThat(result.getPlaceSlotOfMeet().get(0).getUserInfos().get(0).getUsername()).isEqualTo("Test User"),
                () -> assertThat(result.getPlaceSlotOfMeet().get(0).getUserInfos().get(0).getImageUrl()).isEqualTo("test_image_url")
        );
    }

    @Test
    void 모임장소후보지추가컨트롤러정상테스트() throws Exception {
        // given
        String mapx = "mapx";
        String mapy = "mapy";
        String title = "title";
        String roadAddress = "roadAddress";
        String category = "category";
        String link = "link";
        String description = "description";
        String telephone = "telephone";
        String address = "address";
        PlaceInfoDto placeInfoDto = PlaceInfoDto.builder()
                .mapx(mapx)
                .mapy(mapy)
                .title(title)
                .address(address)
                .roadAddress(roadAddress)
                .category(category)
                .link(link)
                .description(description)
                .telephone(telephone)
                .build();

        PlaceSlot mockPlaceSlot = mock(PlaceSlot.class);

        Place place = Place.builder()
                .mapx(mapx)
                .mapy(mapy)
                .title(title)
                .roadAddress(roadAddress)
                .category(category)
                .link(link)
                .description(description)
                .telephone(telephone)
                .build();
        PlaceSlotAppendRequest placeSlotAppendRequest = new PlaceSlotAppendRequest(placeInfoDto);
        when(placeSlotService.appendPlaceSlotFrom(anyLong(), any())).thenReturn(mockPlaceSlot);
        when(mockPlaceSlot.getPlace()).thenReturn(place);

        // when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets/{meetId}/placeslots", 1L)
                        .header("Authorization", "Bearer validToken")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(placeSlotAppendRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        SuccessResponse<NewPlaceSlotResponse> response = objectMapper.readValue(contentAsString, new TypeReference<SuccessResponse<NewPlaceSlotResponse>>() {
        });
        NewPlaceSlotResponse result = response.getResult();

        assertAll(
                () -> assertThat(result.getPlaceInfoDto().getAddress()).isEqualTo(place.getAddress()),
                () -> assertThat(result.getPlaceInfoDto().getTitle()).isEqualTo(place.getTitle()),
                () -> assertThat(result.getPlaceInfoDto().getMapy()).isEqualTo(place.getMapy()),
                () -> assertThat(result.getPlaceInfoDto().getMapx()).isEqualTo(place.getMapx())
        );
    }

    @Test
    void 모임장소후보지추가컨트롤러요청값없을때예외테스트() throws Exception {
        // given
        // when
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/meets/{meetId}/placeslots", 1L)
                        .header("Authorization", "Bearer validToken")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest());
        // then
    }
}