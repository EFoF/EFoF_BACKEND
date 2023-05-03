package com.service.surveyservice.domain.question.exception.response;

import com.service.surveyservice.domain.question.exception.exceptions.QuestionNotFoundException;
import com.service.surveyservice.domain.question.exception.exceptions.QuestionOptionNotFoundException;
import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class QuestionErrorResponse {


    public static final ResponseEntity<ErrorResponse> QUESTION_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(QuestionNotFoundException.class.getSimpleName())
            .message("해당 질문은 존재하지 않는 질문입니다.")
            .build(),HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> QUESTION_OPTION_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(QuestionOptionNotFoundException.class.getSimpleName())
            .message("해당 질문 항목은 존재하지 않는 않습니다.")
            .build(),HttpStatus.NOT_FOUND);

}
