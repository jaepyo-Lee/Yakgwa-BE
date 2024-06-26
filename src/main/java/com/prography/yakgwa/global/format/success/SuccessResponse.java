package com.prography.yakgwa.global.format.success;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Schema(description = "api요청 성공시 반환 규격")
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"time", "status", "code", "message", "result"})
public class SuccessResponse<T> {

    @Schema(description = "http 응답상태 값")
    @JsonProperty("status")
    private int status;
    @Schema(description = "응답 시간")
    private LocalDateTime time;
    @Schema(description = "api응답에 대한 자체 응답코드")
    private String code;
    @Schema(description = "응답 메시지 값")
    private String message;

    @Schema(description = "응답에 대한 반환 결과")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    @Builder
    public SuccessResponse(int status, LocalDateTime time, String code, String message) {
        this.status = status;
        this.time = time;
        this.code = code;
        this.message = message;
    }

    //성공의 경우
    public SuccessResponse(T result) {
        this.status = HttpStatus.OK.value();
        this.time = now();
        this.code = SuccessResponseStatus.SUCCESS.getCode();
        this.message = SuccessResponseStatus.SUCCESS.getMessage();
        this.result = result;
    }

    public static SuccessResponse ok(String message) {
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .time(now())
                .code(SuccessResponseStatus.SUCCESS.getCode())
                .message(message)
                .build();
    }

    public static SuccessResponse ok() {
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .time(now())
                .code(SuccessResponseStatus.SUCCESS.getCode())
                .message("SUCCESS")
                .build();
    }
}
