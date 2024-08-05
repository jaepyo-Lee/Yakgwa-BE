package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetThemeService {
    private final MeetThemeJpaRepository meetThemeJpaRepository;

    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 13:22
     * Finish-Date) 2024-07-29
     */
    public List<MeetTheme> getMeetThemes() {
        return meetThemeJpaRepository.findAll();
    }
}
