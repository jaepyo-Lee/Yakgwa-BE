package com.prography.yakgwa.domain.magazine.impl;

import com.prography.yakgwa.domain.common.impl.AwsS3Util;
import com.prography.yakgwa.domain.magazine.entity.Image;
import com.prography.yakgwa.domain.magazine.entity.Magazine;
import com.prography.yakgwa.domain.magazine.impl.dto.MagazineWriteDto;
import com.prography.yakgwa.domain.magazine.repository.ImageJpaRepository;
import com.prography.yakgwa.domain.magazine.repository.MagazineJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.Role;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class MagazineWriterTest {
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PlaceJpaRepository placeJpaRepository;
    @Autowired
    MagazineJpaRepository magazineJpaRepository;
    @Autowired
    MagazineWriter magazineWriter;
    @Autowired
    ImageJpaRepository imageJpaRepository;
    @Mock
    AwsS3Util awsS3Util;

    @AfterEach
    void init() {
        imageJpaRepository.deleteAll();
        magazineJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        placeJpaRepository.deleteAll();
    }

    @Test
    void 관리자가아닌사용자가매거진작성을요청할때_예외() throws IOException {
        // given
        User saveUser = createAndSaveUser(1, Role.ROLE_USER);
        MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "thumbnail.jpg", "image/jpeg", new byte[]{});
        MockMultipartFile contents = new MockMultipartFile("contents", "contents.jpg", "image/jpeg", new byte[]{});

        MagazineWriteDto writeDto = MagazineWriteDto.of("title", thumbnail, contents);
        Place savePlace = createAndSavePlace(1);

        Mockito.when(awsS3Util.upload(Mockito.any(), Mockito.anyString())).thenReturn("test");
        // when
        System.out.println("=====Logic Start=====");
        System.out.println("=====Logic End=====");
        // then
        assertThrows(RuntimeException.class, () -> magazineWriter.write(savePlace, saveUser, writeDto));
    }

    @Test
    void 관리자가매거진작성을_저장() throws IOException {
        // given
        User saveUser = createAndSaveUser(1, Role.ROLE_ADMIN);
        MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "thumbnail.jpg", "image/jpeg", new byte[]{});
        MockMultipartFile contents = new MockMultipartFile("contents", "contents.jpg", "image/jpeg", new byte[]{});
        String title = "title";
        MagazineWriteDto writeDto = MagazineWriteDto.of(title, thumbnail, contents);
        Place savePlace = createAndSavePlace(1);

        Mockito.when(awsS3Util.upload(Mockito.any(), Mockito.anyString())).thenReturn("test");
        // when
        System.out.println("=====Logic Start=====");
        Magazine write = magazineWriter.write(savePlace, saveUser, writeDto);
        System.out.println("=====Logic End=====");
        // then
        List<Magazine> allMagazine = magazineJpaRepository.findAll();
        List<Image> all = imageJpaRepository.findAll();
        assertAll(
                () -> assertThat(allMagazine.size()).isEqualTo(1),
                () -> assertThat(write.getTitle()).isEqualTo(title),
                () -> assertThat(write.getUser().getId()).isEqualTo(saveUser.getId()),
                () -> assertThat(write.getPlace().getId()).isEqualTo(savePlace.getId()),
                () -> assertThat(write.isOpen()).isTrue(),
                () -> assertThat(all.size()).isEqualTo(2)
        );
    }


    private User createAndSaveUser(int id, Role role) {
        User user = User.builder()
                .role(role).name("name" + id).imageUrl("imageUrl" + id).fcmToken("fcmtoken" + id).authId("authId" + id).authType(AuthType.KAKAO).isNew(true)
                .build();
        return userJpaRepository.save(user);
    }

    private Place createAndSavePlace(int id) {
        Place place = PlaceInfoDto.builder()
                .mapx("" + id).mapy("" + id).link("link" + id).address("address" + id).roadAddress("roadAddress" + id).category("category" + id).description("description" + id).title("title" + id).telephone("telephone" + id)
                .build().toEntity();
        return placeJpaRepository.save(place);
    }
}