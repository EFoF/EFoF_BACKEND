package com.service.surveyservice.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    private final String exceptionName;
    private final String message;

    @Builder
    public ErrorResponse(String exceptionName,  String message) {
        this.exceptionName = exceptionName;
        this.message = message;
    }
}
