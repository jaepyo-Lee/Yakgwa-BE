package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeVoteJpaRepository extends JpaRepository<TimeVote, Long> {

    @Query("select count(*)>0 from TIME_VOTE_TABLE tv where tv.user.id=:userId and tv.timeSlot.meet.id=:meetId")
    boolean existsByUserIdInMeet(@Param("userId") Long userId, @Param("meetId") Long meetId);

    @Query("SELECT TV FROM TIME_VOTE_TABLE TV WHERE TV.user.id = :userId AND TV.timeSlot.meet.id=:meetId")
    List<TimeVote> findAllByTimeSlotOfUser(@Param("userId") Long userId, @Param("meetId")Long meetId);

    @Modifying
    @Query("DELETE FROM TIME_VOTE_TABLE TV WHERE TV.user = :user AND TV.timeSlot.meet.id=:meetId")
    void deleteAllByUserIdAndTimeSlotIdIn(@Param("user") User user, @Param("meetId") Long meetId);

    @Query("SELECT TV FROM TIME_VOTE_TABLE TV WHERE TV.timeSlot.meet.id=:meetId")
    List<TimeVote> findAllByMeetId(@Param("meetId") Long meetId);
}
