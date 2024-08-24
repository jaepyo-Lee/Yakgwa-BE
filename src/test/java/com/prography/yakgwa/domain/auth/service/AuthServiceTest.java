package com.prography.yakgwa.domain.auth.service;

import com.prography.yakgwa.domain.auth.service.request.LoginRequestDto;
import com.prography.yakgwa.domain.auth.service.response.KakaoUserResponseDto;
import com.prography.yakgwa.domain.auth.service.response.LoginResponseDto;
import com.prography.yakgwa.domain.auth.service.response.ReissueTokenSetResponseDto;
import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.Role;
import com.prography.yakgwa.domain.user.entity.SignoutUser;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.SignoutUserJpaRepository;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.client.auth.KakaoClient;
import com.prography.yakgwa.global.entity.AuthToken;
import com.prography.yakgwa.global.format.exception.auth.NotSupportLoginTypeException;
import com.prography.yakgwa.global.format.exception.auth.jwt.InvalidRefreshTokenException;
import com.prography.yakgwa.global.repository.RedisRepository;
import com.prography.yakgwa.global.util.HeaderUtil;
import com.prography.yakgwa.global.util.jwt.TokenProvider;
import com.prography.yakgwa.global.util.jwt.TokenSet;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import com.prography.yakgwa.testHelper.mock.WithCustomMockUser;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    RepositoryDeleter deleter;
    @MockBean
    KakaoClient kakaoClient;
    @Autowired
    DummyCreater dummyCreater;
    @Autowired
    AuthService authService;
    @MockBean
    TokenProvider tokenProvider;

    @Autowired
    private UserJpaRepository userJpaRepository;
    @MockBean
    RedisRepository redisRepository;
    @Autowired
    private SignoutUserJpaRepository signoutUserJpaRepository;

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    //모킹할것 : 카카오통신
    @Test
    void 로그인() {
        // given
        String fcmtoken = "fcmtoken";
        String token = "token";
        AuthType kakao = KAKAO;
        String imageUrl = "imageUrl";
        Long id = 123123L;
        String name = "name";

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .token(token)
                .fcmToken(fcmtoken)
                .loginType(kakao)
                .build();

        KakaoUserResponseDto kakaoUserResponseDto = KakaoUserResponseDto.builder()
                .id(id)
                .properties(KakaoUserResponseDto.Properties.builder()
                        .nickname(name)
                        .build())
                .build();

        when(kakaoClient.getUserData(anyString(), anyString())).thenReturn(kakaoUserResponseDto);

        TokenSet tokenSet = TokenSet.ofBearer("accessToken", "refreshToken");
        when(tokenProvider.createTokenSet(anyString(), anyString(), anyString())).thenReturn(tokenSet);
        when(tokenProvider.getTokenExpiration(tokenSet.getRefreshToken())).thenReturn(50000L);
        when(tokenProvider.getTokenExpiration(tokenSet.getAccessToken())).thenReturn(10000L);
        doNothing().when(redisRepository).refreshSave(anyString(), anyString(), any());
        // when
        System.out.println("=====Logic Start=====");

        LoginResponseDto responseDto = authService.login(loginRequestDto);

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(responseDto.getIsNew()).isTrue(),
                () -> assertThat(responseDto.getRole()).isEqualTo(Role.ROLE_USER),
                () -> assertThat(responseDto.getTokenSet().getAccessToken()).isEqualTo(tokenSet.getAccessToken()),
                () -> assertThat(responseDto.getTokenSet().getRefreshToken()).isEqualTo(tokenSet.getRefreshToken()));
        verify(kakaoClient, times(1)).getUserData(anyString(), anyString());
        verify(redisRepository, times(1)).refreshSave(anyString(), anyString(), any());
    }

    @Test
    void 이미회원가입되어있는회원의로그인() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        String fcmtoken = "fcmtoken";
        String token = "token";
        AuthType kakao = KAKAO;
        String imageUrl = "imageUrl";
        Long id = Long.valueOf(saveUser.getAuthId());
        String name = "name";

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .token(token)
                .fcmToken(fcmtoken)
                .loginType(kakao)
                .build();

        KakaoUserResponseDto kakaoUserResponseDto = KakaoUserResponseDto.builder()
                .id(id)
                .properties(KakaoUserResponseDto.Properties.builder()
                        .nickname(name)
                        .build())
                .build();

        when(kakaoClient.getUserData(anyString(), anyString())).thenReturn(kakaoUserResponseDto);

        TokenSet tokenSet = TokenSet.ofBearer("accessToken", "refreshToken");
        when(tokenProvider.createTokenSet(anyString(), anyString(), anyString())).thenReturn(tokenSet);
        when(tokenProvider.getTokenExpiration(tokenSet.getRefreshToken())).thenReturn(50000L);
        when(tokenProvider.getTokenExpiration(tokenSet.getAccessToken())).thenReturn(10000L);
        doNothing().when(redisRepository).refreshSave(anyString(), anyString(), any());

        // when
        System.out.println("=====Logic Start=====");

        LoginResponseDto responseDto = authService.login(loginRequestDto);
        String refreshToken = redisRepository.getRefreshToken(String.valueOf(id));

        System.out.println("=====Logic End=====");
        // then
        assertAll(() -> assertThat(responseDto.getIsNew()).isTrue(),
                () -> assertThat(responseDto.getRole()).isEqualTo(Role.ROLE_USER),
                () -> assertThat(responseDto.getTokenSet().getAccessToken()).isEqualTo(tokenSet.getAccessToken()),
                () -> assertThat(responseDto.getTokenSet().getRefreshToken()).isEqualTo(tokenSet.getRefreshToken()),
                () -> assertThat(userJpaRepository.findAll().size()).isOne());
        verify(kakaoClient, times(1)).getUserData(anyString(), anyString());
        verify(redisRepository, times(1)).refreshSave(anyString(), anyString(), any());
    }

    @Test
    void 카카오로그인이아닐때() {
        // given
        String fcmtoken = "fcmtoken";
        String token = "token";
        String imageUrl = "imageUrl";
        Long id = 123123L;
        String name = "name";

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .token(token)
                .fcmToken(fcmtoken)
                .loginType(AuthType.APPLE)
                .build();

        KakaoUserResponseDto kakaoUserResponseDto = KakaoUserResponseDto.builder()
                .id(id)
                .properties(KakaoUserResponseDto.Properties.builder()
                        .nickname(name)
                        .build())
                .build();

        when(kakaoClient.getUserData(anyString(), anyString())).thenReturn(kakaoUserResponseDto);

        TokenSet tokenSet = TokenSet.ofBearer("accessToken", "refreshToken");
        when(tokenProvider.createTokenSet(anyString(), anyString(), anyString())).thenReturn(tokenSet);
        when(tokenProvider.getTokenExpiration(tokenSet.getRefreshToken())).thenReturn(50000L);
        when(tokenProvider.getTokenExpiration(tokenSet.getAccessToken())).thenReturn(10000L);

        // when
        System.out.println("=====Logic Start=====");

        assertThrows(NotSupportLoginTypeException.class, () -> authService.login(loginRequestDto));

        System.out.println("=====Logic End=====");
        // then
    }

    @Test
    void 로그인시저장된올바른토큰의재발급() {
        // given
        String ACCESS_TOKEN = "reissueAccessToken";
        String REFRESH_TOKEN = "reissueRefreshToken";

        User saveUser = dummyCreater.createAndSaveUser(1);


        Claims mockClaim = mock(Claims.class);
        AuthToken mockAuthToken = mock(AuthToken.class);
        when(tokenProvider.convertAuthToken(any())).thenReturn(mockAuthToken);
        when(mockAuthToken.getTokenClaims()).thenReturn(mockClaim);
        when(mockClaim.get("userId")).thenReturn(saveUser.getId());
        when(mockClaim.get("loginType")).thenReturn(saveUser.getAuthType());

        when(redisRepository.getRefreshToken(anyString())).thenReturn("refreshToken");
        doNothing().when(redisRepository).removeRefreshToken(anyString());
        TokenSet tokenSet = TokenSet.ofBearer(ACCESS_TOKEN, REFRESH_TOKEN);
        when(tokenProvider.createTokenSet(anyString(), anyString(), anyString())).thenReturn(tokenSet);

        // when
        System.out.println("=====Logic Start=====");

        String refreshToken = "Bearer refreshToken";
        ReissueTokenSetResponseDto responseDto = authService.reissue(refreshToken);

        System.out.println("=====Logic End=====");

        // then
        assertAll(() -> assertThat(responseDto.getReissueRefreshToken()).isEqualTo(REFRESH_TOKEN),
                () -> assertThat(responseDto.getReissueAccessToken()).isEqualTo(ACCESS_TOKEN));
        verify(redisRepository, times(1)).getRefreshToken(anyString());
        verify(redisRepository, times(1)).removeRefreshToken(anyString());
    }

    @Test
    void 로그인시저장된토큰이아닌잘못된토큰을통한재발급() {
        // given
        String ACCESS_TOKEN = "reissueAccessToken";
        String REFRESH_TOKEN = "reissueRefreshToken";

        User saveUser = dummyCreater.createAndSaveUser(1);


        Claims mockClaim = mock(Claims.class);
        AuthToken mockAuthToken = mock(AuthToken.class);
        when(tokenProvider.convertAuthToken(any())).thenReturn(mockAuthToken);
        when(mockAuthToken.getTokenClaims()).thenReturn(mockClaim);
        when(mockClaim.get("userId")).thenReturn(saveUser.getId());
        when(mockClaim.get("loginType")).thenReturn(saveUser.getAuthType());

        when(redisRepository.getRefreshToken(anyString())).thenReturn("refreshToken");
        doNothing().when(redisRepository).removeRefreshToken(anyString());
        TokenSet tokenSet = TokenSet.ofBearer(ACCESS_TOKEN, REFRESH_TOKEN);
        when(tokenProvider.createTokenSet(anyString(), anyString(), anyString())).thenReturn(tokenSet);

        // when
        System.out.println("=====Logic Start=====");

        String wrongRefreshToken = "Bearer refresh";
        assertThrows(InvalidRefreshTokenException.class, () -> authService.reissue(wrongRefreshToken));

        System.out.println("=====Logic End=====");

        // then
    }

    @WithCustomMockUser
    @Test
    void 회원탈퇴() {
        // given
        User saveUser = dummyCreater.createAndSaveUser(1);
        String name = saveUser.getName();
        String accessToken = "Bearer validAccessToken";
        String parsedToken = "validAccessToken";
        long time = new Date().getTime();
        when(tokenProvider.getTokenExpiration(parsedToken)).thenReturn(time + 10000L);

        // when
        System.out.println("=====Logic Start=====");

        authService.signout(saveUser.getId(), accessToken);

        System.out.println("=====Logic End=====");
        // then
        List<SignoutUser> allSignoutUsers = signoutUserJpaRepository.findAll();
        User user = userJpaRepository.findById(saveUser.getId()).get();

        assertAll(() -> assertThat(allSignoutUsers.size()).isOne(),
                () -> assertThat(user.getAuthId()).isNull(),
                () -> assertThat(user.getAuthType()).isNull(),
                () -> assertThat(user.getFcmToken()).isNull(),
                () -> assertThat(user.getName()).isEqualTo(name));
        verify(redisRepository, times(1)).logoutTokenSave(anyString(), any(Duration.class));
        verify(redisRepository, times(1)).getRefreshToken(anyString());
        verify(redisRepository, never()).removeRefreshToken(anyString());
    }


    @WithCustomMockUser
    @Test
    void 로그아웃() {
        //when
        String accessToken = "Bearer validAccessToken";
        String parsedToken = "validAccessToken";
        long time = new Date().getTime();
        when(tokenProvider.getTokenExpiration(parsedToken)).thenReturn(time + 10000L);
        //given

        authService.logout(accessToken);

        //then
        verify(redisRepository, times(1)).logoutTokenSave(anyString(), any(Duration.class));
        verify(redisRepository, times(1)).getRefreshToken(anyString());
        verify(redisRepository, never()).removeRefreshToken(anyString());
    }

    @WithCustomMockUser
    @Test
    void refresh토크이있을때_로그아웃() {
        //when
        String accessToken = "Bearer validAccessToken";
        String parsedToken = "validAccessToken";
        long time = new Date().getTime();
        when(tokenProvider.getTokenExpiration(parsedToken)).thenReturn(time + 10000L);
        when(redisRepository.getRefreshToken(anyString())).thenReturn("existRefresh");

        //given

        authService.logout(accessToken);

        //then
        verify(redisRepository, times(1)).logoutTokenSave(anyString(), any(Duration.class));
        verify(redisRepository, times(1)).getRefreshToken(anyString());
        verify(redisRepository, times(1)).removeRefreshToken(anyString());
    }
}