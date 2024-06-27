package com.prography.yakgwa.domain.participant.repository;

import com.prography.yakgwa.domain.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantJpaRepository extends JpaRepository<Participant,Long> {
}
