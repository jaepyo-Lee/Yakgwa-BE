package com.prography.yakgwa.domain.meet.entity;

public enum MeetStatus {
    CONFIRM, BEFORE_CONFIRM, BEFORE_VOTE, VOTE;

    public boolean isConfirm() {
        return this == CONFIRM;
    }

    public boolean isBeforeConfirm() {
        return this == BEFORE_CONFIRM;
    }

    public boolean isAfterVoteStatus() {
        return isConfirm() || isBeforeConfirm();
    }
}
