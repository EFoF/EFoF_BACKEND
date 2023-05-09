package com.service.surveyservice.domain.token.exception.handler;

import com.service.surveyservice.domain.member.application.AuthService;
import com.service.surveyservice.domain.token.exception.exceptions.ExpiredAccessTokenException;
import com.service.surveyservice.domain.token.exception.exceptions.ExpiredRefreshTokenException;
import com.service.surveyservice.global.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.service.surveyservice.domain.token.exception.response.TokenErrorResponse.ACCESS_TOKEN_EXPIRE;
import static com.service.surveyservice.domain.token.exception.response.TokenErrorResponse.REFRESH_TOKEN_EXPIRE;

@Slf4j
@RestControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler(ExpiredAccessTokenException.class)
    public final ResponseEntity<ErrorResponse> handleExpiredAccessTokenException(ExpiredAccessTokenException ex, WebRequest request) {
        log.info(request.getDescription(false));
        return ACCESS_TOKEN_EXPIRE;
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public final ResponseEntity<ErrorResponse> handleExpiredRefreshTokenException(ExpiredRefreshTokenException ex, WebRequest request) {
        log.info(request.getDescription(false));
        return REFRESH_TOKEN_EXPIRE;
    }
}
