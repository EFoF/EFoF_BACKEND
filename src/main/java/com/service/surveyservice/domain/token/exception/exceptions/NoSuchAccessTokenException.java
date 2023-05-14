package com.service.surveyservice.domain.token.exception.exceptions;

public class NoSuchAccessTokenException extends RuntimeException{
    public NoSuchAccessTokenException() {
        super("헤당 Access token으로 refresh token을 찾을 수 없습니다.");
    }

    public NoSuchAccessTokenException(String message) {
        super(message);
    }
}
