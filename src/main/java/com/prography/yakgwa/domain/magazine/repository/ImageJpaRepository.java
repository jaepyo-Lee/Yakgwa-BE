package com.prography.yakgwa.domain.magazine.repository;

import com.prography.yakgwa.domain.magazine.entity.Image;
import com.prography.yakgwa.domain.magazine.entity.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageJpaRepository extends JpaRepository<Image,Long> {
    /**
     * Todo
     * Work) Test Code
     * Write-Date) 2024-07-28, 일, 1:16
     * Finish-Date)
     */
    List<Image> findAllByMagazineId(@Param("magazineId") Long magazineId);
}
