package com.prography.yakgwa.global.util;

public class HeaderUtil {
    private final static String TOKEN_PREFIX = "Bearer ";

    public static String parseBearer(String bearerToken) {
        if (bearerToken == null) {
            return null;
        }
        String bearer = bearerToken.substring(0, TOKEN_PREFIX.length());
        if(!bearer.equals(TOKEN_PREFIX)){
            return null;
        }
        String token = bearerToken.substring(TOKEN_PREFIX.length());
        if (token.isEmpty()) {
            return null;
        }
        return bearerToken.substring(TOKEN_PREFIX.length());
    }
}
