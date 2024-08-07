package com.prography.yakgwa.domain.common.alarm;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FirebaseMessageSender {

    @Value("${fcm.url}")
    private String API_URL;

    public void sendMessageTo(String message) throws IOException {
        WebClient webClient = WebClient.create();
        webClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .header("Authorization", "Bearer " + getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .retry(3)
                .block();
    }



    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase-key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
