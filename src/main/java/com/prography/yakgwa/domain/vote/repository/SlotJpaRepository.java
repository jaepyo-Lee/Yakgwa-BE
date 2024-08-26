package com.prography.yakgwa.domain.vote.repository;

import com.prography.yakgwa.domain.vote.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotJpaRepository<T extends Slot>extends JpaRepository<T, Long> {
}
