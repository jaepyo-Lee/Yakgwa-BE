package com.prography.yakgwa.domain.user.repository;

import com.prography.yakgwa.domain.user.entity.SignoutUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignoutUserJpaRepository extends JpaRepository<SignoutUser,Long> {
}
