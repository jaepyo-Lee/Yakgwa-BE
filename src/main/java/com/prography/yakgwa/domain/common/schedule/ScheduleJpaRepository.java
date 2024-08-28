package com.prography.yakgwa.domain.common.schedule;

import com.prography.yakgwa.global.format.enumerate.AlarmType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByMeetIdAndAlarmType(@Param("meetId") Long meetId,@Param("alarmType") AlarmType alarmType);
}
