package com.prography.yakgwa.domain.common.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmTestController {
    private final FcmMessageConverter fcmMessageConverter;
    private final FirebaseMessageSender firebaseMessageSender;

    @PostMapping("/test/fcm")
    public void sendMessage(@RequestParam("receiverToken") String receiverToken) throws IOException {
        String message = fcmMessageConverter.makeMessage(receiverToken, "testTitle", "testBody");
        firebaseMessageSender.sendMessageTo(message);
    }
}
