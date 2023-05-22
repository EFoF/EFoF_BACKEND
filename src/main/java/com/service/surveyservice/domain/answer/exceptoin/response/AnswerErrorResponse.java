package com.service.surveyservice.domain.answer.exceptoin.response;

import com.service.surveyservice.domain.answer.exceptoin.exceptions.AuthorOrMemberNotFoundException;
import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AnswerErrorResponse {


    public static final ResponseEntity<ErrorResponse> AUTHOR_MEMBER_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(AuthorOrMemberNotFoundException.class.getSimpleName())
            .message("통계에 대한 권한이 없습니다.")
            .build(),HttpStatus.BAD_REQUEST);


}
