package com.prography.yakgwa.domain.magazine.service;

import com.prography.yakgwa.domain.common.impl.AwsS3Util;
import com.prography.yakgwa.domain.magazine.entity.Image;
import com.prography.yakgwa.domain.magazine.entity.Magazine;
import com.prography.yakgwa.domain.magazine.repository.ImageJpaRepository;
import com.prography.yakgwa.domain.magazine.repository.MagazineJpaRepository;
import com.prography.yakgwa.domain.magazine.service.req.CreateMagazineRequestDto;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.Role;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.format.exception.magazine.NotFoundMagazineException;
import com.prography.yakgwa.global.format.exception.user.NotMatchAdminRoleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.prography.yakgwa.domain.user.entity.Role.ROLE_ADMIN;
import static com.prography.yakgwa.domain.user.entity.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class MagazineServiceTest {
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PlaceJpaRepository placeJpaRepository;
    @Autowired
    private MagazineService magazineService;
    @Autowired
    private MagazineJpaRepository magazineJpaRepository;
    @Autowired
    private ImageJpaRepository imageJpaRepository;
    @MockBean
    AwsS3Util awsS3Util;

    @AfterEach
    void init() {
        imageJpaRepository.deleteAll();
        magazineJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        placeJpaRepository.deleteAll();
    }

    @Test
    void 매거진_생성() throws IOException {
        // given
        MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "thumbnail.jpg", "image/jpeg", new byte[]{});
        MockMultipartFile contents = new MockMultipartFile("contents", "contents.jpg", "image/jpeg", new byte[]{});
        User saveUser = createAndSaveUser(1, ROLE_ADMIN);
        Place savePlace = createAndSavePlace(1);
        CreateMagazineRequestDto requestDto = CreateMagazineRequestDto.builder()
                .title("title").userId(saveUser.getId()).placeId(savePlace.getId())
                .build();
        Mockito.when(awsS3Util.upload(any(MultipartFile.class), Mockito.anyString())).thenReturn("test");
        // when
        System.out.println("=====Logic Start=====");

        Magazine magazine = magazineService.create(requestDto, thumbnail, contents);

        System.out.println("=====Logic End=====");
        // then
        List<Magazine> allMagazine = magazineJpaRepository.findAll();
        List<Image> allImage = imageJpaRepository.findAll();
        assertAll(() -> assertThat(magazine.getTitle()).isEqualTo(requestDto.getTitle()),
                () -> assertThat(magazine.getUser().getId()).isEqualTo(saveUser.getId()),
                () -> assertThat(magazine.getPlace().getId()).isEqualTo(savePlace.getId()),
                () -> assertThat(allMagazine.size()).isEqualTo(1),
                () -> assertThat(allImage.size()).isEqualTo(2));

    }

    @Test
    void 관리자가아닌사용자의매거진생성_예외() throws IOException {
        // given
        MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", "thumbnail.jpg", "image/jpeg", new byte[]{});
        MockMultipartFile contents = new MockMultipartFile("contents", "contents.jpg", "image/jpeg", new byte[]{});
        User saveUser = createAndSaveUser(1, ROLE_USER);
        Place savePlace = createAndSavePlace(1);
        CreateMagazineRequestDto requestDto = CreateMagazineRequestDto.builder()
                .title("title").userId(saveUser.getId()).placeId(savePlace.getId())
                .build();
        Mockito.when(awsS3Util.upload(any(MultipartFile.class), Mockito.anyString())).thenReturn("test");
        // when
        System.out.println("=====Logic Start=====");
        System.out.println("=====Logic End=====");
        // then

        assertThrows(NotMatchAdminRoleException.class, () -> magazineService.create(requestDto, thumbnail, contents));
    }

    @Test
    void 매거진의공개여부수정관리자가아닐경우_예외() {
        // given
        User saveUser = createAndSaveUser(1, ROLE_USER);
        Place savePlace = createAndSavePlace(1);
        Magazine saveMagazine = createAndSaveMagazine(1, saveUser, savePlace);
        // when
        System.out.println("=====Logic Start=====");
        System.out.println("=====Logic End=====");
        // then
        assertThrows(NotMatchAdminRoleException.class, () -> magazineService.modifyOpenState(saveUser.getId(), savePlace.getId()));
    }

    @Test
    void 매거진의상태수정_정상수행() {
        // given
        User saveUser = createAndSaveUser(1, ROLE_ADMIN);
        Place savePlace = createAndSavePlace(1);
        Magazine saveMagazine = createAndSaveMagazine(1, saveUser, savePlace);

        // when
        System.out.println("=====Logic Start=====");

        Magazine magazine = magazineService.modifyOpenState(saveUser.getId(), saveMagazine.getId());

        System.out.println("=====Logic End=====");
        // then
        assertAll(()-> assertThat(!saveMagazine.isOpen()).isEqualTo(magazine.isOpen()));
    }

    private Magazine createAndSaveMagazine(int id, User saveUser, Place savePlace) {
        Magazine magazine = Magazine.builder()
                .user(saveUser).place(savePlace).title("title" + id)
                .build();
        return magazineJpaRepository.save(magazine);
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