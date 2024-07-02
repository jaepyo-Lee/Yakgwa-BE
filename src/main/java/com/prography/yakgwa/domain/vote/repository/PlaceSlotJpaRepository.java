package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceSlotJpaRepository extends JpaRepository<PlaceSlot, Long> {
    List<PlaceSlot> findByMeetId(@Param("meetId") Long meetId);

    @Query("SELECT PS FROM PLACESLOT_TABLE PS WHERE PS.meet.id = :meetId AND PS.confirm = true")
    Optional<PlaceSlot> findConfirmByMeetId(@Param("meetId") Long meetId);
}
