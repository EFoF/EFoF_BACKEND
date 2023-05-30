package com.service.surveyservice.domain.constraintoptions.exception.handler;

import com.service.surveyservice.domain.constraintoptions.exception.exceptions.ConstraintNotFoundException;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.service.surveyservice.domain.constraintoptions.exception.response.ConstraintOptionErrorResponse.CONSTRAINT_NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ConstraintOptionExceptionHandler {

    @ExceptionHandler(ConstraintNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleUserNotFoundException(ConstraintNotFoundException ex, WebRequest request) {
        log.error(request.getDescription(true));
        log.error(String.valueOf(ex.getCause()));
        return CONSTRAINT_NOT_FOUND;
    }


}
