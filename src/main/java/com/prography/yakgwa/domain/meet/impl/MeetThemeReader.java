package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.global.format.exception.meet.NotFoundThemeException;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@ImplService
public class MeetThemeReader {
    private final MeetThemeJpaRepository meetThemeJpaRepository;

    public List<MeetTheme> readAll() {
        return meetThemeJpaRepository.findAll();
    }

    public MeetTheme read(Long meetThemeId) {
        return meetThemeJpaRepository.findById(meetThemeId)
                .orElseThrow(NotFoundThemeException::new);
    }
}
