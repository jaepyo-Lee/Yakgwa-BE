package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.global.format.exception.meet.NotFoundMeetException;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ImplService
public class MeetReader {
    private final MeetJpaRepository meetJpaRepository;
    public Meet read(Long id){
        return meetJpaRepository.findById(id).orElseThrow(NotFoundMeetException::new);
    }
}
