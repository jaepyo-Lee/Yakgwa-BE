package com.prography.yakgwa.domain.common.alarm.repository;

import com.prography.yakgwa.domain.common.alarm.entity.Alarm;
import com.prography.yakgwa.global.format.enumerate.AlarmType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlarmJpaRepository extends JpaRepository<Alarm, Long> {
}
