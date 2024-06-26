package com.prography.yakgwa.global.util.jwt;

import com.prography.yakgwa.global.entity.AuthToken;
import com.prography.yakgwa.global.format.exception.auth.TokenValidFailedException;
import com.prography.yakgwa.global.repository.RedisRepository;
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
import org.springframework.security.core.userdetails.User;
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
    private final RedisRepository redisRepository;

    @Autowired
    public TokenProvider(@Value("${app.auth.secret}") String secret,
                         RedisRepository redisRepository) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.redisRepository = redisRepository;
    }

    /**
     * todo
     * 테스트코드 짜기
     */
    public TokenSet createTokenSet(String userAuthId, String loginType) {
        String accessJwt = createJwt(userAuthId, ACCESS_TOKEN_VALIDATiON_SECOND, loginType);
        String refreshJwt = createJwt(userAuthId, REFRESH_TOKEN_VALIDATiON_SECOND, loginType);
        return TokenSet.ofBearer(accessJwt, refreshJwt);
    }

    private String createJwt(String authId, long duration, String loginType) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + duration);
        return Jwts.builder()
                .setClaims(createClaimByAuthId(authId, loginType))
                .setSubject(authId)
                .setExpiration(expiration)
                .setIssuedAt(now)
                .signWith(key)
                .compact();
    }

    private Map<String, Object> createClaimByAuthId(String authId, String loginType) {
        Map<String, Object> map = new HashMap<>();
        map.put("authId", authId);
        map.put("loginType", loginType);
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
                            .get("authId")
                            .toString()})
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            User principal = new User((String) tokenClaims.get("authId"), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, null, authorities);
        } else {
            throw new TokenValidFailedException();
        }
    }
}
