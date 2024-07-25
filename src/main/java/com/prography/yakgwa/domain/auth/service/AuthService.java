package com.prography.yakgwa.domain.auth.service;

import com.prography.yakgwa.domain.auth.service.request.LoginRequestDto;
import com.prography.yakgwa.domain.auth.service.response.KakaoUserResponseDto;
import com.prography.yakgwa.domain.auth.service.response.LoginResponseDto;
import com.prography.yakgwa.domain.auth.service.response.ReissueTokenSetResponseDto;
import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.client.auth.KakaoClient;
import com.prography.yakgwa.global.format.exception.auth.NotSupportLoginTypeException;
import com.prography.yakgwa.global.format.exception.auth.jwt.InvalidRefreshTokenException;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import com.prography.yakgwa.global.repository.RedisRepository;
import com.prography.yakgwa.global.util.HeaderUtil;
import com.prography.yakgwa.global.util.jwt.TokenProvider;
import com.prography.yakgwa.global.util.jwt.TokenSet;
import lombok.RequiredArgsConstructor;
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
    private final KakaoClient kakaoClient;
    private final UserJpaRepository userJpaRepository;
    private final TokenProvider tokenProvider;
    private final RedisRepository redisRepository;

    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User authUser;
        authUser = getUserByLoginType(requestDto);

        User user = userJpaRepository.findByAuthIdAndAuthType(authUser.getAuthId(), requestDto.getLoginType())
                .orElseGet(() -> userJpaRepository.save(authUser));
        TokenSet tokenSet = tokenProvider.createTokenSet(String.valueOf(user.getId()),user.getName() ,String.valueOf(requestDto.getLoginType()));
        registerRefreshToken(user, tokenSet);
        return LoginResponseDto.builder()
                .role(user.getRole())
                .isNew(user.getIsNew())
                .tokenSet(tokenSet)
                .build();
    }

    private void registerRefreshToken(User user, TokenSet tokenSet) {
        Duration duration = getExpireDuration(tokenSet);
        redisRepository.refreshSave(user.getAuthId(), tokenSet.getRefreshToken(), duration);
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
                    .imageUrl(requestDto.getBaseImage())
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

        String authId = (String) tokenProvider.convertAuthToken(parseToken).getTokenClaims().get("authId");
        String loginType = (String) tokenProvider.convertAuthToken(parseToken).getTokenClaims().get("loginType");

        verifyAndRemoveOriginRefreshToken(refreshToken, authId);

        User findUserByAuthId = userJpaRepository.findByAuthIdAndAuthType(authId, AuthType.valueOf(loginType))
                .orElseThrow(NotFoundUserException::new);

        TokenSet tokenSet = tokenProvider.createTokenSet(findUserByAuthId.getAuthId(), findUserByAuthId.getName(),loginType);

        return ReissueTokenSetResponseDto.builder()
                .reissueAccessToken(tokenSet.getAccessToken())
                .reissueRefreshToken(tokenSet.getRefreshToken())
                .build();
    }

    private void verifyAndRemoveOriginRefreshToken(String refreshToken, String authId) {
        String redisRT = redisRepository.getRefreshToken(authId);

        if (Objects.isNull(redisRT) || !redisRT.equals(refreshToken) || refreshToken.equals("logout")) {
            throw new InvalidRefreshTokenException();
        }
        redisRepository.removeRefreshToken(authId);
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
        String authId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (redisRepository.getRefreshToken(authId) != null) {
            redisRepository.removeRefreshToken(authId);
        }
    }
}
