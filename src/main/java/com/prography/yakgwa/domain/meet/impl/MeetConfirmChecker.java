package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class MeetConfirmChecker {
    private final TimeConfirmChecker timeConfirmChecker;
    private final PlaceConfirmChecker placeConfirmChecker;

    public boolean isMeetConfirm(Meet meet) {
        return timeConfirmChecker.isConfirm(meet) && placeConfirmChecker.isConfirm(meet);
    }
}
