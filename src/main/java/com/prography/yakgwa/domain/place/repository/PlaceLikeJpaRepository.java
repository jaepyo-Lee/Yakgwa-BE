package com.prography.yakgwa.domain.place.repository;

import com.prography.yakgwa.domain.place.entity.PlaceLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceLikeJpaRepository extends JpaRepository<PlaceLike,Long> {
    @Query("select pl from PlaceLike pl join fetch pl.place where pl.user.id=:userId ")
    List<PlaceLike> findAllFetchByUserId(@Param("userId") Long userId);

    List<PlaceLike> findAllByUserId(@Param("userId") Long userId);

    boolean existsByPlaceIdAndUserId(@Param("placeId") Long placeId, @Param("userId") Long userId);
    void deleteByPlaceIdAndUserId(@Param("placeId") Long placeId, @Param("userId") Long userId);

    void deleteAllByUserId(@Param("userId") Long userId);
}
