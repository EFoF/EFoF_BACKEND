package com.service.surveyservice.global.error.exception.response;

import com.service.surveyservice.global.error.ErrorResponse;
import com.service.surveyservice.global.error.exception.NotAuthorizedException;
import com.service.surveyservice.global.error.exception.NotFoundByIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalErrorResponse {

    public static final ResponseEntity<ErrorResponse> NOT_FOUND_BY_ID = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(NotFoundByIdException.class.getSimpleName()).message("해당하는 ID로 대상을 찾을 수 없습니다.").build(), HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> NOT_AUTHORIZED = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(NotAuthorizedException.class.getSimpleName()).message("접근 권한이 없습니다.").build(), HttpStatus.NOT_FOUND);
}
