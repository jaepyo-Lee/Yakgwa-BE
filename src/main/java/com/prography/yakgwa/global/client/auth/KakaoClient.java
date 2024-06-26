package com.prography.yakgwa.global.client.auth;

import com.prography.yakgwa.domain.auth.service.response.KakaoUserResponseDto;
import com.prography.yakgwa.domain.user.entity.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;

@Component
public class KakaoClient implements AuthClient {
    private final WebClient webClient;

    public KakaoClient(String baseUrl){
        this.webClient = WebClient.create(baseUrl);
    }
    public KakaoClient(){
        this.webClient = WebClient.create();
    }

    /**
     * todo
     * 테스트코드 짜기
     */
    @Override
    public User getUserData(String authToken, String authServerUri) {
        KakaoUserResponseDto kakaoUserResponseDto = webClient.post()
                .uri(authServerUri)
                .headers(httpHeaders -> httpHeaders.set("Authorization","Bearer "+authToken))
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(KakaoUserResponseDto.class)
                .block();
        return User.builder()
                .authId(String.valueOf(kakaoUserResponseDto.getId()))
                .authType(KAKAO)
                .name(kakaoUserResponseDto.getProperties().getNickname())
                .isNew(true)
                .build();
    }
}
