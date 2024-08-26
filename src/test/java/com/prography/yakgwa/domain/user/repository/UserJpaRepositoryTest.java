package com.prography.yakgwa.domain.user.repository;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.testHelper.config.DeleterConfig;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserJpaRepositoryTest extends IntegrationTestSupport {
    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 사용자의AuthId와AuthType으로_조회Test() {
        // given
        String authId = "authId";
        AuthType authType = AuthType.KAKAO;
        User user = User.builder()
                .name("name").authId(authId).authType(authType).fcmToken("fcmToken").imageUrl("imageUrl")
                .build();
        userJpaRepository.save(user);

        // when
        System.out.println("=====Logic Start=====");

        Optional<User> byAuthIdAndAndAuthType = userJpaRepository.findByAuthIdAndAuthType(authId, authType);

        System.out.println("=====Logic End=====");
        // then
        assertThat(byAuthIdAndAndAuthType).isPresent();

    }

    @Test
    void 존재하지않는사용자의AuthId와AuthType으로_조회Test() {
        // given
        String authId = "authId";
        AuthType authType = AuthType.KAKAO;

        // when
        System.out.println("=====Logic Start=====");

        Optional<User> byAuthIdAndAndAuthType = userJpaRepository.findByAuthIdAndAuthType(authId, authType);

        System.out.println("=====Logic End=====");

        // then
        assertThat(byAuthIdAndAndAuthType).isEmpty();

    }
}