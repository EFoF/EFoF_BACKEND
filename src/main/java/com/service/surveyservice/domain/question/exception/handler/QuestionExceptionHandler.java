package com.service.surveyservice.domain.question.exception.handler;

import com.service.surveyservice.domain.question.exception.exceptions.QuestionNotFoundException;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.service.surveyservice.domain.question.exception.response.QuestionErrorResponse.*;

@Slf4j
@RestControllerAdvice
public class QuestionExceptionHandler {

    @ExceptionHandler(QuestionNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleQuestionNotFoundException(QuestionNotFoundException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return QUESTION_NOT_FOUND;
    }



}
