package com.service.surveyservice.domain.member.exception.exceptions.member;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("유효하지 않은 refresh token 입니다.");
    }

    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
