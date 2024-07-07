package com.prography.yakgwa.domain.participant.impl;

import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ImplService
@RequiredArgsConstructor
public class ParticipantReader {
    private final ParticipantJpaRepository repository;

    public List<Participant> readAllByMeetId(Long meetId) {
        return repository.findAllByMeetId(meetId);
    }

    public List<Participant> readAllByUserId(Long userId) {
        return repository.findAllByUserId(userId);
    }

    public Participant readByUserIdAndMeetId(Long userId, Long meetId) {
        return repository.findByUserIdAndMeetId(userId, meetId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
    }
}
