package com.service.surveyservice.domain.survey.exception.response;

import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SurveyErrorResponse {

    public static final ResponseEntity<ErrorResponse> SURVEY_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND)
            .message("해당 설문은 존재하지 않는 설문입니다.")
            .build(),HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> SURVEY_MEMBER_MISMATCH = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST)
            .message("설문 생성자만 추가할 수 있습니다.")
            .build(),HttpStatus.BAD_REQUEST);

}
