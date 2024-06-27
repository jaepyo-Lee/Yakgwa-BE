package com.prography.yakgwa.domain.user.impl;

import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class UserReader {
    private final UserJpaRepository userJpaRepository;
    public User read(Long userId){
        return userJpaRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }
}
