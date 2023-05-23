package com.service.surveyservice.domain.token.exception.exceptions;

public class ExpiredRefreshTokenException extends RuntimeException{
    public ExpiredRefreshTokenException() {
        super("Refresh Token이 만료되었습니다.");
    }

    public ExpiredRefreshTokenException(String s) {
        super(s);
    }
}
