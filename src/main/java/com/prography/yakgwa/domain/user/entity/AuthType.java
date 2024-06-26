package com.prography.yakgwa.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthType {
    /**
     * 애플 서버 uri 고쳐놓기
     */
    KAKAO("https://kapi.kakao.com/v2/user/me"),APPLE("not yet");
    private String serverUri;
}
