package com.prography.yakgwa.domain.user.impl;

import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class UserReaderTest {
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    UserReader userReader;

    @AfterEach
    void init() {
        userJpaRepository.deleteAll();
    }

    @Test
    void 존재하는유저_조회Test() {
        // given
        User user = User.builder()
                .name("user").fcmToken("fcmtoken").authType(AuthType.KAKAO).isNew(true).imageUrl("imageUrl")
                .build();
        User saveUser = userJpaRepository.save(user);

        // when
        System.out.println("=====Logic Start=====");

        User compare = userReader.read(saveUser.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(compare.getName()).isEqualTo(user.getName()),
                () -> assertThat(compare.getAuthId()).isEqualTo(user.getAuthId()),
                () -> assertThat(compare.getAuthType()).isEqualTo(user.getAuthType()));
    }

    @Test
    void 존재하지않는유저_조회Test() {
        // given
        Long notExistUserId = 1L;
        // when
        // then
        System.out.println("=====Logic Start=====");

        assertThrows(NotFoundUserException.class, () -> userReader.read(notExistUserId));

        System.out.println("=====Logic End=====");
    }
}