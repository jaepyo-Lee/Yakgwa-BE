package com.prography.yakgwa.domain.user.controller.res;

import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.Role;
import com.prography.yakgwa.domain.user.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserInfoResponseTest {
    @Test
    void of테스트() {
        // given
        User user = User.builder()
                .name("name").imageUrl("imageUrl").isNew(true).fcmToken("fcmtoken").role(Role.ROLE_ADMIN).authType(AuthType.KAKAO).id(1L)
                .build();
        UserInfoResponse userInfoResponse = UserInfoResponse.of(user);

        // when
        // then
        System.out.println("=====Logic Start=====");

        assertAll(() -> assertThat(userInfoResponse.getImageUrl()).isEqualTo(user.getImageUrl()),
                () -> assertThat(userInfoResponse.getName()).isEqualTo(user.getName()));

        System.out.println("=====Logic End=====");
    }
}