package com.service.surveyservice.domain.section.exception.response;

import com.service.surveyservice.domain.question.exception.exceptions.QuestionNotFoundException;
import com.service.surveyservice.domain.section.exception.exceptions.SectionNotFoundException;
import com.service.surveyservice.domain.section.exception.exceptions.SectionQuestionMissMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyMemberMisMatchException;
import com.service.surveyservice.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SectionErrorResponse {

    public static final ResponseEntity<ErrorResponse> SECTION_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(SectionNotFoundException.class.getSimpleName())
            .message("해당 섹션은 존재하지 않는 섹션입니다.")
            .build(),HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> SURVEY_NOT_MATCH_SECTION = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(SurveyMemberMisMatchException.class.getSimpleName())
            .message("요청한 설문과 다른 섹션입니다.")
            .build(),HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<ErrorResponse> QUESTION_NOT_MATCH_SECTION = new ResponseEntity<>(ErrorResponse.builder()
            .exceptionName(SectionQuestionMissMatchException.class.getSimpleName())
            .message("요청한 질문문과 다른 섹션입니다.")
            .build(),HttpStatus.BAD_REQUEST);


}
