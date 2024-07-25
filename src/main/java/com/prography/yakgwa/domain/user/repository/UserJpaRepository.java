package com.prography.yakgwa.domain.user.repository;

import com.prography.yakgwa.domain.user.entity.AuthType;
import com.prography.yakgwa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByAuthIdAndAuthType(String authId, AuthType authType);
}
