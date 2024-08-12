package com.prography.yakgwa.domain.vote.service;

import com.prography.yakgwa.domain.meet.entity.Meet;

public interface VoteConfirm {
    boolean confirmMaxOf(Meet meet);
}
