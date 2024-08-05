package com.prography.yakgwa.global.format.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static java.time.LocalDateTime.now;

@Getter
public class ErrorResponse {
    private LocalDateTime time;
    private int status;
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FieldError> errors;


    private ErrorResponse(ApplicationRunException e){
        this.time = now();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.code = e.getErrorEnumCode().getCode();
        this.message = e.getMessage();
    }

    private ErrorResponse(MethodArgumentNotValidException e){
        this.time = now();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.code = e.getBindingResult().getFieldError().getCode();
        this.message = getValidationErrorMessage(e);
    }
    private ErrorResponse(DateTimeParseException e){
        this.time = now();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.code = HttpStatus.BAD_REQUEST.name();
        this.message = e.getMessage();
    }

    private static String getValidationErrorMessage(MethodArgumentNotValidException e) {
        return e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    }

    public static ErrorResponse of(MethodArgumentNotValidException e){
        return new ErrorResponse(e);
    }

    public static ErrorResponse of(DateTimeParseException e){
        return new ErrorResponse(e);
    }

    public static ErrorResponse of(ApplicationRunException e) {
        return new ErrorResponse(e);
    }
}
