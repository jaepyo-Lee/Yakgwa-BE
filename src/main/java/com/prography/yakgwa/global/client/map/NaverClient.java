package com.prography.yakgwa.global.client.map;

import com.prography.yakgwa.domain.place.service.dto.NaverMapResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NaverClient {
    private final WebClient webClient = WebClient.create("https://openapi.naver.com");

    @Value("${naver.client.id}")
    private String NAVER_ID;
    @Value("${naver.client.secret}")
    private String NAVER_SECRET;

    public NaverMapResponseDto searchNaverAPIClient(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/search/local.json")
                        .queryParam("query", query)
                        .queryParam("display", 5)
                        .build())
                .header("Accept", "application/json")
                .header("X-Naver-Client-Id", NAVER_ID)
                .header("X-Naver-Client-Secret", NAVER_SECRET)
                .retrieve()
                .bodyToMono(NaverMapResponseDto.class)
                .block();
    }
}
