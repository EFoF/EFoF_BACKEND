package com.service.surveyservice.domain.survey.exception.response;

import com.service.surveyservice.domain.member.exception.exceptions.member.UserNotFoundException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyMemberMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SurveyErrorResponse {

    public static final ResponseEntity<ErrorResponse> SURVEY_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(SurveyNotFoundException.class.getSimpleName())
            .message("해당 설문은 존재하지 않는 설문입니다.")
            .build(),HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> SURVEY_MEMBER_MISMATCH = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(SurveyMemberMisMatchException.class.getSimpleName())
            .message("설문 생성자와 일치하지 않습니다.")
            .build(),HttpStatus.NOT_FOUND);

}
