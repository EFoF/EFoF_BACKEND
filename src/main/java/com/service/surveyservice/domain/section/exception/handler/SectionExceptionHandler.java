package com.service.surveyservice.domain.section.exception.handler;

import com.service.surveyservice.domain.section.exception.exceptions.SurveyMissMatchException;
import com.service.surveyservice.domain.section.exception.exceptions.SectionNotFoundException;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.service.surveyservice.domain.section.exception.response.SectionErrorResponse.*;

@Slf4j
@RestControllerAdvice
public class SectionExceptionHandler {

    @ExceptionHandler(SectionNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleSectionNotFoundException(SectionNotFoundException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return SECTION_NOT_FOUND;
    }


    @ExceptionHandler(SurveyMissMatchException.class)
    protected final ResponseEntity<ErrorResponse> handleSurveyMismatchException(SurveyMissMatchException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return SURVEY_NOT_MATCH_SECTION;
    }
}
