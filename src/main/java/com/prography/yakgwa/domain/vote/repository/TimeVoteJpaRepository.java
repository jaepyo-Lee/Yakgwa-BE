package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeVoteJpaRepository extends JpaRepository<TimeVote, Long> {
    boolean existsByUserId(Long userId);

    @Query("select count(*)>0 from TIMESLOT_TABLE as ts where ts.confirm=true and ts.meet.id=:meetId")
    boolean existsByMeetId(@Param("meetId") Long meetId);

    @Query("SELECT TV FROM TIME_VOTE_TABLE TV WHERE TV.user.id = :userId AND TV.timeSlot.id IN :timeSlotIds")
    List<TimeVote> findAllByTimeSlotOfUser(@Param("userId") Long userId, @Param("timeSlotIds") List<Long> timeSlotIds);

    @Modifying
    @Query("DELETE FROM TIME_VOTE_TABLE TV WHERE TV.user = :user AND TV.timeSlot IN :timeSlots")
    void deleteAllByUserIdAndTimeSlotIdIn(@Param("user") User user, @Param("timeSlots") List<TimeSlot> timeSlots);
}
