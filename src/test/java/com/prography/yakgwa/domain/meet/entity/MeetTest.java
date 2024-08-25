package com.prography.yakgwa.domain.meet.entity;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MeetTest extends IntegrationTestSupport {
    @Autowired
    RepositoryDeleter deleter;
    @Autowired
    MeetJpaRepository meetJpaRepository;

    @AfterEach
    void init() {
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

    @Test
    void id로동등성비교() {
        // given
        Meet meet = Meet.builder().id(1L).build();
        Meet compare = Meet.builder().id(1L).build();
        Meet compare2 = Meet.builder().id(2L).build();
        // when
        System.out.println("=====Logic Start=====");

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(meet.equals(compare)).isTrue(),
                () -> assertThat(meet.equals(compare2)).isFalse());
    }
}