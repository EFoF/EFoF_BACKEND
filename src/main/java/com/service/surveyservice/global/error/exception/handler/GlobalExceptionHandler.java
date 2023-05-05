package com.service.surveyservice.global.error.exception.handler;

import com.service.surveyservice.global.error.ErrorResponse;
import com.service.surveyservice.global.error.exception.NotAuthorizedException;
import com.service.surveyservice.global.error.exception.NotFoundByIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.service.surveyservice.global.error.exception.response.GlobalErrorResponse.NOT_AUTHORIZED;
import static com.service.surveyservice.global.error.exception.response.GlobalErrorResponse.NOT_FOUND_BY_ID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundByIdException.class)
    protected final ResponseEntity<ErrorResponse> handleNotFoundByIdException(NotFoundByIdException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return NOT_FOUND_BY_ID;
    }

    @ExceptionHandler(NotAuthorizedException.class)
    protected final ResponseEntity<ErrorResponse> handleNotAuthorizedException(NotAuthorizedException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return NOT_AUTHORIZED;
    }


    //@ExceptionHandler(UserNotFoundException.class)
    //    protected final ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
    //        log.error(request.getDescription(false));
    //        return USER_NOT_FOUND;
    //    }
}
