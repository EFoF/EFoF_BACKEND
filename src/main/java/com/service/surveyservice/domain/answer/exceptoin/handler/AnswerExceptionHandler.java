package com.service.surveyservice.domain.answer.exceptoin.handler;

import com.service.surveyservice.domain.answer.exceptoin.exceptions.AuthorOrMemberNotFoundException;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.service.surveyservice.domain.answer.exceptoin.response.AnswerErrorResponse.AUTHOR_MEMBER_NOT_FOUND;
import static com.service.surveyservice.domain.question.exception.response.QuestionErrorResponse.*;

@Slf4j
@RestControllerAdvice
public class AnswerExceptionHandler {

    @ExceptionHandler(AuthorOrMemberNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleAuthenticationException
            (AuthorOrMemberNotFoundException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return AUTHOR_MEMBER_NOT_FOUND;
    }




}
