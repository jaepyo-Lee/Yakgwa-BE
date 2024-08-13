package com.prography.yakgwa.domain.meet.entity;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.global.config.AuditingConfig;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.testHelper.config.DeleterConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

class MeetTest extends IntegrationTestSupport {
    @Autowired
    RepositoryDeleter deleter;
    @Autowired
    MeetJpaRepository meetJpaRepository;

    @AfterEach
    void init(){
        deleter.deleteAll();
    }

    @Test
    void 투표가능시간조회() {
        // given
        Meet meet = Meet.builder().validInviteHour(24).build();
        Meet saveMeet = meetJpaRepository.save(meet);
        // when
        System.out.println("=====Logic Start=====");

        System.out.println("=====Logic End=====");
        // then
        assertThat(saveMeet.getVoteTime()).isEqualTo(saveMeet.getCreatedDate().plusHours(24));
    }

    @Test
    void 확정안되었을때투표가능시간조회() {
        // given
        Meet meet = Meet.builder().validInviteHour(24).build();
        Meet saveMeet = meetJpaRepository.save(meet);
        // when
        System.out.println("=====Logic Start=====");


        System.out.println("=====Logic End=====");
        // then
        assertThat(saveMeet.getConfirmTime()).isEqualTo(saveMeet.getCreatedDate().plusHours(24).plusHours(24));
    }
    @Test
    void 투표가능시간이안지났울떄() {
        // given
        Meet meet = Meet.builder().validInviteHour(24).build();
        Meet saveMeet = meetJpaRepository.save(meet);
        // when
        System.out.println("=====Logic Start=====");


        System.out.println("=====Logic End=====");
        // then
        assertThat(saveMeet.isVoteTimeEnd()).isFalse();
    }

    @Test
    void 투표가능시간이지났을때() {
        // given
        Meet meet = Meet.builder().validInviteHour(-25).build();
        Meet saveMeet = meetJpaRepository.save(meet);
        // when
        System.out.println("=====Logic Start=====");


        System.out.println("=====Logic End=====");
        // then
        assertThat(saveMeet.isVoteTimeEnd()).isTrue();
    }

}