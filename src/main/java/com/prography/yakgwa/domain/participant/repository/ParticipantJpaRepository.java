package com.prography.yakgwa.domain.participant.repository;

import com.prography.yakgwa.domain.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantJpaRepository extends JpaRepository<Participant, Long> {
    List<Participant> findAllByMeetId(@Param("meetId") Long meetId);

    List<Participant> findAllByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndMeetId(@Param("userId") Long userId, @Param("meetId") Long meetId);
}
