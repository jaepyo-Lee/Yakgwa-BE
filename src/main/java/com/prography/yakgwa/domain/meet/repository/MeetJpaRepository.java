package com.prography.yakgwa.domain.meet.repository;

import com.prography.yakgwa.domain.meet.entity.Meet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetJpaRepository extends JpaRepository<Meet,Long> {
}
