package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotJpaRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByMeetId(Long meetId);

    @Query("SELECT TS FROM TIMESLOT_TABLE TS WHERE TS.meet.id = :meetId AND TS.confirm = true")
    Optional<TimeSlot> findConfirmByMeetId(@Param("meetId") Long meetId);
}
