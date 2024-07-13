package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotJpaRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByMeetId(Long meetId);

    List<TimeSlot> findAllByMeetId(Long meetId);

    @Query("SELECT TS FROM TIMESLOT_TABLE TS WHERE TS.meet.id = :meetId AND TS.confirm = true")
    Optional<TimeSlot> findConfirmByMeetId(@Param("meetId") Long meetId);

    @Query("select count(*)>0 from TIMESLOT_TABLE as ts where ts.confirm=true and ts.meet.id=:meetId")
    boolean existsByMeetId(@Param("meetId") Long meetId);

    @Query("SELECT ts FROM TIMESLOT_TABLE ts WHERE ts.meet.id = :meetId AND ts.time IN (:times)")
    List<TimeSlot> findAllByMeetIdAndTimes(@Param("meetId") Long meetId, @Param("times") List<LocalDateTime> times);
}
