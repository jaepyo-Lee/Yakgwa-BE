package com.prography.yakgwa.domain.place.repository;

import com.prography.yakgwa.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceJpaRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByMapxAndMapy(String mapx, String mapy);

    @Query("select p from PLACE_TABLE as p where p.title=:title and p.mapx=:mapx and p.mapy=:mapy")
    Optional<Place> findByTitleAndMapxAndMapy(@Param("title") String title, @Param("mapx") String mapx, @Param("mapy") String mapy);
}
