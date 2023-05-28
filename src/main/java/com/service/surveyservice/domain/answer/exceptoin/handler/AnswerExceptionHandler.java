package com.service.surveyservice.domain.answer.exceptoin.handler;

import com.service.surveyservice.domain.answer.exceptoin.exceptions.*;
import com.service.surveyservice.domain.token.exception.exceptions.NoSuchCookieException;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.service.surveyservice.domain.answer.exceptoin.response.AnswerErrorResponse.*;
import static com.service.surveyservice.domain.question.exception.response.QuestionErrorResponse.*;
import static com.service.surveyservice.domain.survey.exception.response.SurveyErrorResponse.SURVEY_NOT_FOUND;
import static com.service.surveyservice.domain.token.exception.response.TokenErrorResponse.ACCESS_TOKEN_EXPIRE;
import static com.service.surveyservice.domain.token.exception.response.TokenErrorResponse.NO_SUCH_COOKIE;

@Slf4j
@RestControllerAdvice
public class AnswerExceptionHandler {

    @ExceptionHandler(AuthorOrMemberNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleAuthenticationException
            (AuthorOrMemberNotFoundException ex, WebRequest request) {
        log.error(request.getDescription(false));
        return AUTHOR_MEMBER_NOT_FOUND;
    }

    @ExceptionHandler(NoSuchCookieAnswerException.class)
    public final ResponseEntity<ErrorResponse> handleNoSuchCookieAnswerException(NoSuchCookieAnswerException ex, WebRequest request) {
        log.info(request.getDescription(false));
        return NO_SUCH_COOKIE_ANSWER;
    }

    @ExceptionHandler(ExpiredAccessTokenAnswerException.class)
    public final ResponseEntity<ErrorResponse> handleExpiredAccessTokenAnswerException(ExpiredAccessTokenAnswerException ex, WebRequest request) {
        log.info(request.getDescription(false));
        return ACCESS_TOKEN_EXPIRE_ANSWER;
    }

    @ExceptionHandler(SurveyNotFoundAnswerException.class)
    public final ResponseEntity<ErrorResponse> handleSurveyNotFoundAnswerException(SurveyNotFoundAnswerException ex, WebRequest request) {
        log.info(request.getDescription(false));
        return SURVEY_NOT_FOUND_ANSWER;
    }

    @ExceptionHandler(DuplicatedParticipateException.class)
    public final ResponseEntity<ErrorResponse> handleDuplicatedParticipateException(DuplicatedParticipateException ex, WebRequest request) {
        log.info(request.getDescription(false));
        return DUPLICATED_PARTICIPATE;
    }

    @ExceptionHandler(NoNecessaryAnswerException.class)
    public final ResponseEntity<ErrorResponse> handleNoNecessaryAnswerException(NoNecessaryAnswerException ex, WebRequest request) {
        log.info(request.getDescription(false));
        return NO_NECESSARY_ANSWER;
    }
}
