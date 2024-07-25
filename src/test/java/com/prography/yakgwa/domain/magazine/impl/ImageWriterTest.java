package com.prography.yakgwa.domain.magazine.impl;

import com.prography.yakgwa.domain.common.impl.AwsS3Util;
import com.prography.yakgwa.domain.magazine.entity.Magazine;
import com.prography.yakgwa.domain.magazine.repository.ImageJpaRepository;
import com.prography.yakgwa.domain.magazine.repository.MagazineJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ImageWriterTest {
    @Autowired
    ImageJpaRepository imageRepository;

    @Autowired
    MagazineJpaRepository magazineJpaRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    private PlaceJpaRepository placeJpaRepository;

    @MockBean
    AwsS3Util awsS3Util;

    @Autowired
    ImageWriter imageWriter;


    @AfterEach
    void init(){
        imageRepository.deleteAll();
        magazineJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        placeJpaRepository.deleteAll();
    }

    @Test
    void 이미지S3에올리고파일정보엔티티_저장() throws IOException {
        // given
        User saveUser = createAndSaveUser(1L);
        Place savePlace = createAndSavePlace(1L);

        Mockito.when(awsS3Util.upload(Mockito.any(MultipartFile.class), Mockito.anyString())).thenReturn("test");

        Magazine saveMagazine = createAndSaveMagazine(savePlace, saveUser);

        // Create mock MultipartFile instances
        MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "thumbnail.jpg", "image/jpeg", new byte[]{});
        MockMultipartFile contents = new MockMultipartFile("contents", "contents.jpg", "image/jpeg", new byte[]{});

        // when
        System.out.println("=====Logic Start=====");

        imageWriter.write(saveMagazine,thumbnail,contents);

        System.out.println("=====Logic End=====");
        // then
        assertThat(imageRepository.findAll().size()).isEqualTo(2);
    }

    private Magazine createAndSaveMagazine(Place savePlace, User saveUser) {
        Magazine magazine = Magazine.builder()
                .title("title").place(savePlace).user(saveUser)
                .build();

        return magazineJpaRepository.save(magazine);
    }

    private User createAndSaveUser(Long id) {
        User user = User.builder()
                .name("name" + id).imageUrl("imageUrl" + id).fcmToken("fcmtoken" + id).authId("authId" + id).authType(AuthType.KAKAO).isNew(true)
                .build();
        return userJpaRepository.save(user);
    }
    private Place createAndSavePlace(Long id) {
        Place place = PlaceInfoDto.builder()
                .mapx("" + id).mapy("" + id).link("link" + id).address("address" + id).roadAddress("roadAddress" + id).category("category" + id).description("description" + id).title("title" + id).telephone("telephone" + id)
                .build().toEntity();
        return placeJpaRepository.save(place);
    }
}