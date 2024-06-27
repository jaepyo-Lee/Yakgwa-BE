package com.prography.yakgwa.domain.place.repository;

import com.prography.yakgwa.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceJpaRepository extends JpaRepository<Place,Long> {
    Optional<Place> findByMapxAndMapy(String mapx, String mapy);
}
