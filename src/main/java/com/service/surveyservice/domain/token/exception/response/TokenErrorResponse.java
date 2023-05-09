package com.service.surveyservice.domain.token.exception.response;

import com.service.surveyservice.domain.token.exception.exceptions.ExpiredAccessTokenException;
import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TokenErrorResponse {

    public static final ResponseEntity<ErrorResponse> ACCESS_TOKEN_EXPIRE = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(ExpiredAccessTokenException.class.getSimpleName())
            .message("Expired Access Token")
            .build(), HttpStatus.UNAUTHORIZED);
}
