package com.prography.yakgwa.domain.user.service;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.common.impl.AwsS3Util;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import com.prography.yakgwa.testHelper.mock.WithCustomMockUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest extends IntegrationTestSupport {


    @Value("${user.base.image}")
    private String baseImg;

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 사용자조회() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);

        // when
        System.out.println("=====Logic Start=====");
        User user = userService.find(saveUser.getId());
        System.out.println("=====Logic End=====");

        // then
        assertThat(user.getId()).isEqualTo(saveUser.getId());
    }

    @Test
    void 존재하지않는사용자조회_예외() {
        // given
        // when
        // then
        System.out.println("=====Logic Start=====");
        assertThrows(NotFoundUserException.class, () -> userService.find(1L));
        System.out.println("=====Logic End=====");
    }

    @Test
    void 사용자정보수정() throws IOException {
        // given

        User saveUser = dummyCreater.createAndSaveUser(1);

        // when
        System.out.println("=====Logic Start=====");

        userService.modify(null, saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        User user = userJpaRepository.findById(saveUser.getId()).orElse(saveUser);
        assertThat(user.getImageUrl()).isEqualTo(baseImg);
    }

    @Test
    void 새로운이미지로변경하는경우_사용자정보수정() throws IOException {
        // given

        User saveUser = dummyCreater.createAndSaveUser(1);
        String uploadimage = "uploadimage";
        String fileName = "testFile.txt";
        String originalFileName = "testFile.txt";
        String contentType = "text/plain";
        byte[] content = "Hello, World!".getBytes();

        MockMultipartFile mockMultipartFile = new MockMultipartFile(fileName, originalFileName, contentType, content);
        when(awsS3Util.upload(any(),any())).thenReturn(uploadimage);
        // when
        System.out.println("=====Logic Start=====");

        userService.modify(mockMultipartFile, saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        User user = userJpaRepository.findById(saveUser.getId()).orElse(saveUser);
        assertThat(user.getImageUrl()).isEqualTo(uploadimage);
    }
    @WithCustomMockUser
    @Test
    void FCM갱신() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);

        // when
        System.out.println("=====Logic Start=====");

        String newFcmToken = "newFcmToken";
        boolean b = userService.updateFcm(saveUser.getId(), newFcmToken);

        System.out.println("=====Logic End=====");
        // then
        User user = userJpaRepository.findById(saveUser.getId()).get();

        assertAll(() -> assertThat(user.getFcmToken()).isEqualTo(newFcmToken),
                () -> assertThat(b).isTrue());
    }
}