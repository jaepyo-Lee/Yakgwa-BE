package com.prography.yakgwa.domain.auth.service;

import com.prography.yakgwa.domain.common.IntegrationTestSupport;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.global.repository.RedisRepository;
import com.prography.yakgwa.global.util.HeaderUtil;
import com.prography.yakgwa.global.util.jwt.TokenProvider;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest extends IntegrationTestSupport {

    @AfterEach
    void init() {
        deleter.deleteAll();
    }

    @Test
    void 회원탈퇴() {
        String accessToken = "Bearer validAccessToken";
        String parsedToken = "validAccessToken";

        // Mocking the HeaderUtil.parseBearer method
        try (MockedStatic<HeaderUtil> headerUtilMockedStatic = mockStatic(HeaderUtil.class)) {
            headerUtilMockedStatic.when(() -> HeaderUtil.parseBearer(accessToken)).thenReturn(parsedToken);
            // Mocking the tokenProvider.getTokenExpiration method
            when(tokenProvider.getTokenExpiration(anyString())).thenReturn(new Date().getTime() + 100000L);

            // Mocking SecurityContextHolder
            SecurityContextImpl securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("authId", null));
            SecurityContextHolder.setContext(securityContext);

            // Mocking the RedisRepository methods
            when(redisRepository.getRefreshToken("authId")).thenReturn("someRefreshToken");

            authService.logout(accessToken);
        }


        // Use ArgumentMatchers to verify method calls with flexible argument matching
        verify(redisRepository).logoutTokenSave(anyString(), any(Duration.class));
        verify(redisRepository).removeRefreshToken("authId");
    }
}