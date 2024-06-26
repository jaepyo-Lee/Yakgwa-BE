package com.prography.yakgwa.domain.meet.service;

import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.impl.MeetThemeReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MeetThemeService {
    private final MeetThemeReader meetThemeReader;

    public List<MeetTheme> getMeetThemes() {
        return meetThemeReader.readAll();
    }
}
