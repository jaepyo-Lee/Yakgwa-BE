package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceVoteJpaRepository extends JpaRepository<PlaceVote, Long> {
    List<PlaceVote> findAllByUserId(Long userId);

    boolean existsByUserId(@Param("userId") Long userId);

    @Query("SELECT PV FROM PLACE_VOTE_TABLE PV WHERE PV.user.id = :userId AND PV.placeSlot.id IN :placeSlotIds")
    List<PlaceVote> findAllByPlaceSlotOfUser(@Param("userId") Long userId, @Param("placeSlotIds") List<Long> placeSlotIds);

    @Modifying
    @Query("DELETE FROM PLACE_VOTE_TABLE PV WHERE PV.user = :user AND PV.placeSlot IN :placeSlots")
    void deleteAllByUserIdAndPlaceSlotIdIn(@Param("user") User user, @Param("placeSlots") List<PlaceSlot> placeSlotIds);
}
