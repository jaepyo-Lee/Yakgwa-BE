package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceSlotJpaRepository extends JpaRepository<PlaceSlot, Long> {
    List<PlaceSlot> findAllByMeetId(@Param("meetId") Long meetId);

    @Query("SELECT PS FROM PLACESLOT_TABLE PS WHERE PS.meet.id = :meetId AND PS.confirm = true")
    Optional<PlaceSlot> findConfirmByMeetId(@Param("meetId") Long meetId);

    @Query("select count(*)>0 from PLACESLOT_TABLE as ps where ps.confirm=true and ps.meet.id=:meetId")
    boolean existsByMeetId(@Param("meetId") Long meetId);
}
