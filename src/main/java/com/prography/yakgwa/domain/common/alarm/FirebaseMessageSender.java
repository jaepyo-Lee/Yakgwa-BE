package com.prography.yakgwa.domain.common.alarm;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FirebaseMessageSender {

    @Value("${fcm.url}")
    private String API_URL;

    /**
     * 원래 동기였던거를 비동기로 처리함 -> 그에대한 이점 생각해보기! 자소서에 녹이면 좋을듯!
    */
    public void sendMessageTo(String message) throws IOException {
        WebClient webClient = WebClient.create();
        webClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .header("Authorization", "Bearer " + getAccessToken())
                .retrieve()
                .bodyToMono(String.class).subscribe();
    }



    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase-key.json";

        // try-with-resources 사용하여 InputStream 자원 관리
        try (InputStream inputStream = new ClassPathResource(firebaseConfigPath).getInputStream()) {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(inputStream)
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (FileNotFoundException e) {
            throw new IOException("Firebase configuration file not found at path: " + firebaseConfigPath, e);
        } catch (IOException e) {
            throw new IOException("Failed to load Google Credentials from file: " + firebaseConfigPath, e);
        }
    }
}
