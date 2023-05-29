package com.service.surveyservice.domain.constraintoptions.exception.response;

import com.service.surveyservice.domain.constraintoptions.exception.exceptions.ConstraintNotFoundException;
import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ConstraintOptionErrorResponse {

    public static final ResponseEntity<ErrorResponse> CONSTRAINT_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(ConstraintNotFoundException.class.getSimpleName())
            .message("해당 제약조건은 존재하지 않습니다.")
            .build(),HttpStatus.NOT_FOUND);



}
