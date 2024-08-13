package com.prography.yakgwa.domain.user.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.domain.user.controller.res.UserInfoResponse;
import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.Role;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.service.UserService;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import com.prography.yakgwa.testHelper.mock.WithCustomMockUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;

    @WithCustomMockUser
    @Test
    void 사용자정보조회컨트롤러() throws Exception {
        // given

        User user = User.builder()
                .id(1L).name("username").isNew(true).role(Role.ROLE_USER).authId("123123").authType(AuthType.KAKAO).fcmToken("token").imageUrl("image")
                .build();
        when(userService.find(anyLong())).thenReturn(user);

        // when
        System.out.println("=====Logic Start=====");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        System.out.println("=====Logic End=====");
        // then

        SuccessResponse<UserInfoResponse> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<SuccessResponse<UserInfoResponse>>() {
                });

        UserInfoResponse result = response.getResult();
        assertAll(() -> Assertions.assertThat(result.getName()).isEqualTo(user.getName()),
                () -> Assertions.assertThat(result.getImageUrl()).isEqualTo(user.getImageUrl()));
    }

    @WithCustomMockUser
    @Test
    void 사용자의이미지변경컨트롤러테스트() throws Exception {
        // given

        User user = User.builder()
                .id(1L).name("username").isNew(true).role(Role.ROLE_USER).authId("123123").authType(AuthType.KAKAO).fcmToken("token").imageUrl("image")
                .build();

        MockMultipartFile file = new MockMultipartFile("userImage", "image.jpg", "image/jpeg", "test image content".getBytes());
        doNothing().when(userService).modify(eq(file), anyLong());

        // when
        System.out.println("=====Logic Start=====");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/user/image")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        System.out.println("=====Logic End=====");
        // then
        verify(userService).modify(any(MultipartFile.class), anyLong());
    }

    @WithCustomMockUser
    @Test
    void 사용자의이미지변경컨트롤러_이미지안넣었을때테스트() throws Exception {
        // given

        User user = User.builder()
                .id(1L).name("username").isNew(true).role(Role.ROLE_USER).authId("123123").authType(AuthType.KAKAO).fcmToken("token").imageUrl("image")
                .build();

        MockMultipartFile file = new MockMultipartFile("userImage", "image.jpg", "image/jpeg", "test image content".getBytes());
        doNothing().when(userService).modify(eq(file), anyLong());

        // when
        System.out.println("=====Logic Start=====");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/user/image")
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header("Authorization", "Bearer validToken"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        System.out.println("=====Logic End=====");
        // then
    }
}