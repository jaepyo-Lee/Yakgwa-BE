package com.prography.yakgwa.domain.participant.repository;

import com.prography.yakgwa.domain.participant.entity.Participant;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface ParticipantJpaRepository extends JpaRepository<Participant, Long> {
    List<Participant> findAllByMeetId(@Param("meetId") Long meetId);

    @Query("select p from PARTICIPANT_TABLE p join fetch p.user where p.meet.id=:meetId")
    List<Participant> findAllWithUserByMeetId(@Param("meetId") Long meetId);

    List<Participant> findAllByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndMeetId(@Param("userId") Long userId, @Param("meetId") Long meetId);


    Optional<Participant> findByUserIdAndMeetId(Long userId, Long meetId);
}
