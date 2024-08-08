package com.prography.yakgwa.domain.vote.service;

import java.util.List;

public interface VoteExecuter<T, R> {
    List<T> vote(Long userId, Long meetId, R request);

    void confirm(Long meetId,Long userId,Long slotId);
}
