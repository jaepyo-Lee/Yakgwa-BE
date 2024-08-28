package com.prography.yakgwa.domain.common.alarm;

import com.prography.yakgwa.domain.common.schedule.TaskScheduleManager;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FcmTestController {
    private final MeetJpaRepository meetJpaRepository;
    private final TaskScheduleManager taskScheduleManager;

    @PostMapping("/test/fcm")
    public void sendMessage(@RequestParam("receiverToken") String receiverToken) throws IOException {
        log.info("테스트 동작");

        Meet meet1 = meetJpaRepository.findById(1L).orElse(null);
        taskScheduleManager.regist(meet1,AlarmType.PROMISE_DAY);
    }
}
