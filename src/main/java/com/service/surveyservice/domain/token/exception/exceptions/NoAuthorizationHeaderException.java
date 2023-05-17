package com.service.surveyservice.domain.token.exception.exceptions;

public class NoAuthorizationHeaderException extends RuntimeException{
    public NoAuthorizationHeaderException() {
        super("인증관련 헤더가 존재하지 않습니다.");
    }

    public NoAuthorizationHeaderException(String message) {
        super(message);
    }
}
