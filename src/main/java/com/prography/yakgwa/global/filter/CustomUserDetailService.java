package com.prography.yakgwa.global.filter;

import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserReader userReader;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
        User user = userReader.read(Long.valueOf(username));
        return new CustomUserDetail(user.getId(),user.getName());
    }
}
