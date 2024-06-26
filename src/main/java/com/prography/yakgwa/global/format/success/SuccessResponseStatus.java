package com.prography.yakgwa.global.format.success;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessResponseStatus {
    SUCCESS("Success", "요청에 성공하였습니다.");

    private final String code;
    private final String message;
}
