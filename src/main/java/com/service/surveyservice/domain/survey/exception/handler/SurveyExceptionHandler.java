package com.service.surveyservice.domain.survey.exception.handler;

import com.service.surveyservice.domain.survey.exception.exceptions.SurveyMemberMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyPreMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveySectionMisMatchException;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
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
        log.error(request.getDescription(true));
        log.error(String.valueOf(ex.getCause()));
        return SURVEY_NOT_FOUND;
    }

    @ExceptionHandler(SurveyMemberMisMatchException.class)
    protected final ResponseEntity<ErrorResponse> handleSurveyMemberMisMatchExceptionException(SurveyMemberMisMatchException ex, WebRequest request) {
        log.error(request.getDescription(true));
        log.error(String.valueOf(ex.getCause()));
        return SURVEY_MEMBER_MISMATCH;
    }
    @ExceptionHandler(SurveySectionMisMatchException.class)
    protected final ResponseEntity<ErrorResponse> handleSurveySectionMisMatchException(SurveySectionMisMatchException ex, WebRequest request) {
        log.error(request.getDescription(true));
        log.error(String.valueOf(ex.getCause()));
        return SURVEY_SECTION_MISMATCH;
    }

    @ExceptionHandler(SurveyPreMisMatchException.class)
    protected final ResponseEntity<ErrorResponse> handleSurveyPreMisMatchException(SurveyPreMisMatchException ex, WebRequest request) {
        log.error(request.getDescription(true));
        log.error(String.valueOf(ex.getCause()));
        return SURVEY_PRE_MISMATCH;
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    protected final ResponseEntity<ErrorResponse> handleFileSizeLimitExceededException(FileSizeLimitExceededException ex, WebRequest request) {
        log.error(request.getDescription(true));
        log.error("업로드 이미지 용량 초과");
        log.error(String.valueOf(ex.getCause()));
        return FILE_SIZE_EXCEED;
    }
}
