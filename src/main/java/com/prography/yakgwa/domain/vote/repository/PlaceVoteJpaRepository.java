package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceVoteJpaRepository extends JpaRepository<PlaceVote, Long> {
    List<PlaceVote> findAllByUserId(Long userId);

    boolean existsByUserId(@Param("userId") Long userId);
}
