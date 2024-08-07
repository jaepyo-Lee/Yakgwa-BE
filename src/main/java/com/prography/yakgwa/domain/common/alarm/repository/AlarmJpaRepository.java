package com.prography.yakgwa.domain.common.alarm.repository;

import com.prography.yakgwa.domain.common.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlarmJpaRepository extends JpaRepository<Alarm, Long> {
}
