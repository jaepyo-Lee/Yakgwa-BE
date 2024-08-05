package com.prography.yakgwa.global.format.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationRunException.class)
    public ResponseEntity<ErrorResponse> applicationRunException(ApplicationRunException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object processValidationError(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(e));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public Object dateFormatValidationError(DateTimeParseException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(e));
    }
}
