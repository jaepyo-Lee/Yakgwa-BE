package com.prography.yakgwa.testHelper.mock;

import com.prography.yakgwa.global.filter.CustomUserDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;

public class WithCustomMockUserSecurityContextFactory implements
        WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        long id = annotation.id();
        String name = annotation.name();

        CustomUserDetail customUserDetail = new CustomUserDetail(id, name);
        List<SimpleGrantedAuthority> authorityList = Arrays.stream(new String[]{String.valueOf(id)})
                .map(SimpleGrantedAuthority::new)
                .toList();
        Authentication auth = new UsernamePasswordAuthenticationToken(customUserDetail, "", authorityList);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        return context;
    }
}
