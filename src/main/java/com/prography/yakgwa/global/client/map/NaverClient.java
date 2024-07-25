package com.prography.yakgwa.global.client.map;

import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.service.dto.NaverMapResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NaverClient {
    private final WebClient webClient;

    @Value("${naver.client.id}")
    private String naverId;

    @Value("${naver.client.secret}")
    private String naverSecret;

    public NaverClient() {
        this.webClient = WebClient.create("https://openapi.naver.com");
    }

    public NaverMapResponseDto searchNaverAPIClient(String query) {
        NaverMapResponseDto responseDto = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/search/local.json")
                        .queryParam("query", query)
                        .queryParam("display", 5)
                        .build())
                .header("Accept", "application/json")
                .header("X-Naver-Client-Id", naverId)
                .header("X-Naver-Client-Secret", naverSecret)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.createException().flatMap(Mono::error)
                )
                .bodyToMono(NaverMapResponseDto.class)
                .block();  // 동기적으로 결과를 기다림

        // HTML 태그를 제거합니다.
        cleanHtml(responseDto);
        return responseDto;
    }

    private void cleanHtml(NaverMapResponseDto responseDto) {
        if (responseDto != null && responseDto.getItems() != null) {
            responseDto.getItems().forEach(PlaceInfoDto::removeHtmlOfTitle);
        }
    }
}
