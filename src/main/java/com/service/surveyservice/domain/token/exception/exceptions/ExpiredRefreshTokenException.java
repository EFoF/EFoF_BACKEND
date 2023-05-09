package com.service.surveyservice.domain.token.exception.exceptions;

public class ExpiredRefreshTokenException extends RuntimeException{
    public ExpiredRefreshTokenException() {
        super("RefreshToken이 만료되었습니다.");
    }

    public ExpiredRefreshTokenException(String s) {
        super(s);
    }
}
