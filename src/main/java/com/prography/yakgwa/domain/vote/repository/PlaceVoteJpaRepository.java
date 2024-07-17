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

    @Query("select pv from PLACE_VOTE_TABLE pv where pv.placeSlot.meet.id=:meetId")
    List<PlaceVote> findAllInMeet(@Param("meetId") Long meetId);

    List<PlaceVote> findAllByUserId(Long userId);

    @Query("select count(*)>0 from PLACE_VOTE_TABLE pv where pv.user.id=:userId and pv.placeSlot.meet.id=:meetId")
    boolean existsByUserIdAndMeetId(@Param("userId") Long userId, @Param("meetId") Long meetId);

    @Query("SELECT PV FROM PLACE_VOTE_TABLE PV WHERE PV.user.id = :userId AND PV.placeSlot.meet.id=:meetId")
    List<PlaceVote> findAllByUserIdAndMeetId(@Param("userId") Long userId, @Param("meetId") Long meetId);

    @Modifying
    @Query("DELETE FROM PLACE_VOTE_TABLE PV WHERE PV.user = :user AND PV.placeSlot.meet.id=:meetId")
    void deleteAllByUserIdAndMeetId(@Param("user") User user, @Param("meetId") Long meetId);

    @Query("select pv from PLACE_VOTE_TABLE pv join fetch pv.user where pv.placeSlot.id=:placeSlotId ")
    List<PlaceVote>findAllByPlaceSlotId(@Param("placeSlotId")Long placeSlotId);
}
