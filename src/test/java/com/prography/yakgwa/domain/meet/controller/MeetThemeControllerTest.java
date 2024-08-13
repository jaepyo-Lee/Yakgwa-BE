package com.prography.yakgwa.domain.meet.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.domain.meet.controller.res.MeetThemeResponse;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.service.MeetThemeService;
import com.prography.yakgwa.global.format.success.SuccessResponse;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.testHelper.mock.WithCustomMockUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithCustomMockUser
class MeetThemeControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    MeetThemeService meetThemeService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebApplicationContext wac;
    @Autowired
    DummyCreater dummyCreater;
    @Autowired
    RepositoryDeleter deleter;

    @Test
    void 테마조회컨트롤러테스트() throws Exception {
        // given
        MeetTheme theme = MeetTheme.builder().name("데이트").id(1L).build();
        MeetTheme theme1 = MeetTheme.builder().name("데이트").id(1L).build();
        MeetTheme theme2 = MeetTheme.builder().name("데이트").id(1L).build();
        MeetTheme theme3 = MeetTheme.builder().name("데이트").id(1L).build();

        List<MeetTheme> themes = List.of(theme, theme1, theme2, theme3);
        when(meetThemeService.getMeetThemes()).thenReturn(themes);

        // when
        System.out.println("=====Logic Start=====");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/v1/theme")
                        .header("Authorization", "Bearer validToken")
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        String contentAsString = mvcResult.getResponse().getContentAsString();
        SuccessResponse<List<MeetThemeResponse>> listSuccessResponse =
                objectMapper.readValue(contentAsString, new TypeReference<SuccessResponse<List<MeetThemeResponse>>>() {
                });

        List<MeetThemeResponse> result = listSuccessResponse.getResult();

        System.out.println("=====Logic End=====");
        // then
        MeetThemeResponse build = MeetThemeResponse.builder().id(theme.getId()).name(theme.getName()).build();
        MeetThemeResponse build1 = MeetThemeResponse.builder().id(theme1.getId()).name(theme1.getName()).build();
        MeetThemeResponse build2 = MeetThemeResponse.builder().id(theme2.getId()).name(theme2.getName()).build();
        MeetThemeResponse build3 = MeetThemeResponse.builder().id(theme3.getId()).name(theme3.getName()).build();

        List<MeetThemeResponse> compare = List.of(build, build1, build2, build3);

        assertThat(result).usingRecursiveComparison().isEqualTo(compare);
    }
}