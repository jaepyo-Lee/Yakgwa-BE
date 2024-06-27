package com.prography.yakgwa.domain.meet.repository;

import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetThemeJpaRepository extends JpaRepository<MeetTheme, Long> {
}
