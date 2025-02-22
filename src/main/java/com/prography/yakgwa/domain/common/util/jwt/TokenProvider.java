package com.prography.yakgwa.domain.common.util.jwt;

import com.prography.yakgwa.global.entity.AuthToken;
import com.prography.yakgwa.global.filter.CustomUserDetail;
import com.prography.yakgwa.global.format.exception.auth.TokenValidFailedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    @Value("${app.auth.accessExpired}")
    private long ACCESS_TOKEN_VALIDATiON_SECOND;
    //28일
    @Value("${app.auth.refreshExpired}")
    private long REFRESH_TOKEN_VALIDATiON_SECOND = ACCESS_TOKEN_VALIDATiON_SECOND;
    private Key key;

    @Autowired
    public TokenProvider(@Value("${app.auth.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public TokenSet createTokenSet(String  userId, String username, String loginType) {
        String accessJwt = createJwt(userId, username, ACCESS_TOKEN_VALIDATiON_SECOND, loginType);
        String refreshJwt = createJwt(userId, username, REFRESH_TOKEN_VALIDATiON_SECOND, loginType);
        return TokenSet.ofBearer(accessJwt, refreshJwt);
    }

    private String createJwt(String userId, String username, long duration, String loginType) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + duration);
        return Jwts.builder()
                .setClaims(createClaimByAuthId(userId, username, loginType))
                .setSubject("yakgwa")
                .setExpiration(expiration)
                .setIssuedAt(now)
                .signWith(key)
                .compact();
    }

    private Map<String, Object> createClaimByAuthId(String userId, String username, String loginType) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("loginType", loginType);
        map.put("username", username);
        return map;
    }

    public AuthToken convertAuthToken(String token) {
        return AuthToken.of(token, key);
    }

    public Long getTokenExpiration(String token) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return body.getExpiration().getTime();
    }

    public Authentication getAuthentication(AuthToken authToken) {
        if (authToken.validate()) {
            Claims tokenClaims = authToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities = Arrays.stream(new String[]{tokenClaims
                            .get("userId")
                            .toString()})
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            CustomUserDetail principal = new CustomUserDetail(Long.valueOf((String) tokenClaims.get("userId")), tokenClaims.get("username").toString());
//            User principal = new User((String) tokenClaims.get("userId"), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, null, authorities);
        } else {
            throw new TokenValidFailedException();
        }
    }
}
