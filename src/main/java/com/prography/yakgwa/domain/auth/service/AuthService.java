package com.prography.yakgwa.domain.auth.service;

import com.prography.yakgwa.domain.auth.service.request.LoginRequestDto;
import com.prography.yakgwa.domain.auth.service.response.KakaoUserResponseDto;
import com.prography.yakgwa.domain.auth.service.response.LoginResponseDto;
import com.prography.yakgwa.domain.auth.service.response.ReissueTokenSetResponseDto;
import com.prography.yakgwa.domain.user.entity.SignoutUser;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.SignoutUserJpaRepository;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.common.client.auth.KakaoClient;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.exception.auth.NotSupportLoginTypeException;
import com.prography.yakgwa.global.format.exception.auth.jwt.InvalidRefreshTokenException;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import com.prography.yakgwa.domain.common.redis.RedisRepository;
import com.prography.yakgwa.domain.common.util.HeaderUtil;
import com.prography.yakgwa.domain.common.util.jwt.TokenProvider;
import com.prography.yakgwa.domain.common.util.jwt.TokenSet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {
    public static final String REFRESH_ID = "REFRESHID:";
    private final KakaoClient kakaoClient;
    private final UserJpaRepository userJpaRepository;
    private final TokenProvider tokenProvider;
    private final RedisRepository redisRepository;
    private final SignoutUserJpaRepository signoutUserJpaRepository;
    @Value("${user.base.image}")
    private String BASE_IMAGE;


    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User authUser;
        authUser = getUserByLoginType(requestDto);

        User user = userJpaRepository.findByAuthIdAndAuthType(authUser.getAuthId(), requestDto.getLoginType())
                .orElseGet(() -> userJpaRepository.save(authUser));

        TokenSet tokenSet = tokenProvider.createTokenSet(String.valueOf(user.getId()), user.getName(), String.valueOf(requestDto.getLoginType()));
        registerRefreshToken(user, tokenSet);
        return LoginResponseDto.builder()
                .role(user.getRole())
                .isNew(user.getIsNew())
                .tokenSet(tokenSet)
                .build();
    }

    private void registerRefreshToken(User user, TokenSet tokenSet) {
        Duration duration = getExpireDuration(tokenSet);
        redisRepository.refreshSave(REFRESH_ID + user.getId(), tokenSet.getRefreshToken(), duration);
    }

    private Duration getExpireDuration(TokenSet tokenSet) {
        Long accessExpire = tokenProvider.getTokenExpiration(tokenSet.getAccessToken());
        Long refreshExpire = tokenProvider.getTokenExpiration(tokenSet.getRefreshToken());
        Duration duration = Duration.ofMillis(refreshExpire - accessExpire);
        return duration;
    }

    private User getUserByLoginType(LoginRequestDto requestDto) {
        if (requestDto.getLoginType().equals(KAKAO)) {
            KakaoUserResponseDto userData = kakaoClient.getUserData(requestDto.getToken(), requestDto.getLoginType().getServerUri());
            return User.builder()
                    .imageUrl(BASE_IMAGE)
                    .authId(String.valueOf(userData.getId()))
                    .authType(KAKAO)
                    .name(userData.getProperties().getNickname())
                    .isNew(true)
                    .fcmToken(requestDto.getFcmToken())
                    .build();
        }
        throw new NotSupportLoginTypeException();
    }

    public ReissueTokenSetResponseDto reissue(String refreshToken) {
        String parseToken = HeaderUtil.parseBearer(refreshToken);

        String userId = String.valueOf(tokenProvider.convertAuthToken(parseToken).getTokenClaims().get("userId"));
        String loginType = String.valueOf(tokenProvider.convertAuthToken(parseToken).getTokenClaims().get("loginType"));

        verifyAndRemoveOriginRefreshToken(parseToken, userId);

        User findUser = userJpaRepository.findById(Long.valueOf(userId))
                .orElseThrow(NotFoundUserException::new);

        TokenSet tokenSet = tokenProvider.createTokenSet(String.valueOf(findUser.getId()), findUser.getName(), loginType);

        return ReissueTokenSetResponseDto.builder()
                .reissueAccessToken(tokenSet.getAccessToken())
                .reissueRefreshToken(tokenSet.getRefreshToken())
                .build();
    }

    private void verifyAndRemoveOriginRefreshToken(String refreshToken, String userId) {
        String redisRT = redisRepository.getRefreshToken(REFRESH_ID + userId);

        if (Objects.isNull(redisRT) || !redisRT.equals(refreshToken) || refreshToken.equals("logout")) {
            throw new InvalidRefreshTokenException();
        }
        redisRepository.removeRefreshToken(REFRESH_ID + userId);
    }

    public void logout(String accessToken) {
        String parseToken = HeaderUtil.parseBearer(accessToken);

        Long tokenExpiration = tokenProvider.getTokenExpiration(parseToken);
        Long expireTTL = tokenExpiration - new Date().getTime();

        removeRefreshTokenForBlockingReissue();
        if (expireTTL > 0) {
            redisRepository.logoutTokenSave(parseToken, Duration.ofMillis(expireTTL));
        }
    }

    private void removeRefreshTokenForBlockingReissue() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail details = (CustomUserDetail)authentication.getPrincipal();
        Long userId = details.getUserId();
        if (redisRepository.getRefreshToken(REFRESH_ID + userId) != null) {
            redisRepository.removeRefreshToken(REFRESH_ID + userId);
        }
    }

    @Transactional
    public void signout(Long userId, String accessToken) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);

        SignoutUser signoutUser = SignoutUser.builder()
                .name(user.getName()).authType(KAKAO).imageUrl(user.getImageUrl()).authId(user.getAuthId()).userId(user.getId()).modifiedDate(user.getModifiedDate()).createdDate(user.getCreatedDate())
                .build();
        signoutUserJpaRepository.save(signoutUser);

        user.signout(BASE_IMAGE);
        logout(accessToken);
    }
}
