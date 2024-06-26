package com.prography.yakgwa.global.entity;

import com.prography.yakgwa.global.format.exception.auth.jwt.ExpiredJwtTokenException;
import io.jsonwebtoken.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;

@Slf4j
@Builder
@Getter
@RequiredArgsConstructor
public class AuthToken {
    private final String token;

    private final Key key;


    public static AuthToken of(String token,Key key){
        return AuthToken.builder()
                .token(token)
                .key(key)
                .build();
    }

    public boolean validate(){
        return getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new ExpiredJwtTokenException();
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }
}
