package com.service.surveyservice.domain.section.exception.response;

import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SectionErrorResponse {

    public static final ResponseEntity<ErrorResponse> SECTION_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND)
            .message("해당 섹션은 존재하지 않는 섹션입니다.")
            .build(),HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> SURVEY_NOT_MATCH_SECTION = new ResponseEntity<>(ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST)
            .message("요청한 설문과 다른 섹션입니다.")
            .build(),HttpStatus.BAD_REQUEST);



}
