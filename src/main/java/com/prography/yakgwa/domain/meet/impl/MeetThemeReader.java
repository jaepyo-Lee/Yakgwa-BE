package com.prography.yakgwa.domain.meet.impl;

import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
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
                .orElseThrow(() -> new RuntimeException("존재하지 않는 모임의 테마입니다. 요청을 확인해주세요"));
    }
}
