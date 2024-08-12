package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.common.schedule.TaskScheduleManager;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.impl.MeetConfirmChecker;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.prography.yakgwa.global.format.enumerate.AlarmType.PROMISE_DAY;

@Service
public class ConfirmAllFacade {
    private final MeetJpaRepository meetJpaRepository;
    private final VoteConfirm placeConfirm;
    private final VoteConfirm timeConfirm;
    private final MeetConfirmChecker meetConfirmChecker;
    private final TaskScheduleManager taskScheduleExecuter;

    @Autowired
    public ConfirmAllFacade(MeetJpaRepository meetJpaRepository, PlaceConfirm placeConfirm, TimeConfirm timeConfirm, MeetConfirmChecker meetConfirmChecker, TaskScheduleManager taskScheduleExecuter) {
        this.meetJpaRepository = meetJpaRepository;
        this.placeConfirm = placeConfirm;
        this.timeConfirm = timeConfirm;
        this.meetConfirmChecker = meetConfirmChecker;
        this.taskScheduleExecuter = taskScheduleExecuter;
    }

    public void calConfirm(Long meetId) {
        Meet meet = meetJpaRepository.findById(meetId).orElseThrow(NotFoundMeetException::new);
        placeConfirm.confirmMaxOf(meet);
        timeConfirm.confirmMaxOf(meet);
        if(meetConfirmChecker.isMeetConfirm(meet)){
            taskScheduleExecuter.registerAlarm(meet, PROMISE_DAY);
        }
    }
}
