package com.service.surveyservice.domain.question.exception.response;

import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class QuestionErrorResponse {

    public static final ResponseEntity<ErrorResponse> QUESTION_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND)
            .message("해당 질문은 존재하지 않는 질문입니다.")
            .build(),HttpStatus.NOT_FOUND);



}
