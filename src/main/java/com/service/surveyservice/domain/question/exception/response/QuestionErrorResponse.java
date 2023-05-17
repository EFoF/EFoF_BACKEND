package com.service.surveyservice.domain.question.exception.response;

import com.service.surveyservice.domain.question.exception.exceptions.*;
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

    public static final ResponseEntity<ErrorResponse> QUESTION_OPTION_IMAGE_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(QuestionOptionImageNotFoundException.class.getSimpleName())
            .message("해당 항목에 이미지가 존재하지 않습니다..")
            .build(),HttpStatus.NOT_FOUND);
    public static final ResponseEntity<ErrorResponse> QUESTION_ORDER_EXCEPTION = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(QuestionOrderException.class.getSimpleName())
            .message("섹션 질문에 오류가 발생했습니다.")
            .build(),HttpStatus.BAD_REQUEST);
    public static final ResponseEntity<ErrorResponse> QUESTION_SECTION_MISMATCH = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(QuestionSectionMisMatchException.class.getSimpleName())
            .message("올바르지 않은 질문의 섹션입니다.")
            .build(),HttpStatus.BAD_REQUEST);

}
