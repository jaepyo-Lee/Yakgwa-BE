package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.domain.vote.service.ConfirmAllFacade;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class AfterVoteTaskExectorFacade {
    private final AlarmProcessor alarmProcessor;
    private final ConfirmAllFacade confirmAllFacadeExecutor;

    /**
     * 1. 모임 확정할수 있는지 체크
     * 2. 알람 보내기
    */
    public void execute(Long meetId,AlarmType type){
        confirmAllFacadeExecutor.calConfirm(meetId);
        alarmProcessor.createAlarmTask(meetId, type);
    }
}
