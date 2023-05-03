package com.service.surveyservice.domain.survey.exception.handler;

import com.service.surveyservice.domain.survey.exception.exceptions.SurveyMemberMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.service.surveyservice.domain.survey.exception.response.SurveyErrorResponse.*;

@Slf4j
@RestControllerAdvice
public class SurveyExceptionHandler {

    @ExceptionHandler(SurveyNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleUserNotFoundException(SurveyNotFoundException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return SURVEY_NOT_FOUND;
    }

    @ExceptionHandler(SurveyMemberMisMatchException.class)
    protected final ResponseEntity<ErrorResponse> handleSurveyMemberMisMatchExceptionException(SurveyMemberMisMatchException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return SURVEY_MEMBER_MISMATCH;
    }

}
