package com.prography.yakgwa.domain.user.entity;

import org.junit.jupiter.api.Test;

import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class UserTest {
    @Test
    void 사용자의이미지변경기능_테스트() {
        // given
        User user = User.builder()
                .name("user").authId("authId1").authType(KAKAO).imageUrl("original")
                .build();

        // when
        System.out.println("=====Logic Start=====");

        String changeImg = "change";
        user.changeImage(changeImg);

        System.out.println("=====Logic End=====");
        // then
        assertThat(user.getImageUrl()).isEqualTo(changeImg);

    }
    @Test
    void 사용자탈퇴() {
        // given
        User user = User.builder()
                .name("user").authId("authId1").authType(KAKAO).imageUrl("original")
                .build();
        String baseImage = "baseImage";

        // when
        System.out.println("=====Logic Start=====");

        user.signout(baseImage);

        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(user.getAuthId()).isNull(),
                ()-> assertThat(user.getFcmToken()).isNull(),
                ()-> assertThat(user.getName()).isEqualTo(user.getName()),
                ()-> assertThat(user.getAuthType()).isNull(),
                ()-> assertThat(user.getImageUrl()).isEqualTo(baseImage)
                );
    }

    @Test
    void 사용자FCM토큰갱신() {
        // given
        String newFCM = "new";
        User user = User.builder().fcmToken("old").build();

        // when
        System.out.println("=====Logic Start=====");

        user.refreshFcm(newFCM);

        System.out.println("=====Logic End=====");
        // then
        assertThat(user.getFcmToken()).isEqualTo(newFCM);

    }
}