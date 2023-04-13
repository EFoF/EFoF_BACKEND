package com.service.surveyservice.domain.token.exception;

public class ExpiredRefreshTokenException extends IllegalArgumentException{
    public ExpiredRefreshTokenException() {
        super("RefreshToken이 만료되었습니다.");
    }

    public ExpiredRefreshTokenException(String s) {
        super(s);
    }
}
