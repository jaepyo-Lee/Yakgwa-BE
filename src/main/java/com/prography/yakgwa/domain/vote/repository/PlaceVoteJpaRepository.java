package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.vote.entity.PlaceVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceVoteJpaRepository extends JpaRepository<PlaceVote,Long> {
}
