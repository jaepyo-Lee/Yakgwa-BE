package com.prography.yakgwa.domain.magazine.repository;

import com.prography.yakgwa.domain.magazine.entity.Magazine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineJpaRepository extends JpaRepository<Magazine,Long> {
    Page<Magazine> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
