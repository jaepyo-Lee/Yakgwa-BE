package com.prography.yakgwa.global.client.auth;

import com.prography.yakgwa.domain.user.entity.User;

public interface AuthClient {
    User getUserData(String token, String authServerUri);
}
