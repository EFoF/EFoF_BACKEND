package com.service.surveyservice.domain.token.exception.exceptions;

public class ExpiredAccessTokenException extends RuntimeException{
    public ExpiredAccessTokenException() {
        super("Access Token이 만료되었습니다.");
    }

    public ExpiredAccessTokenException(String message) {
        super(message);
    }
}
