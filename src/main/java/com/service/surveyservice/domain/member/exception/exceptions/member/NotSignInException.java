package com.service.surveyservice.domain.member.exception.exceptions.member;

public class NotSignInException extends RuntimeException {
    public NotSignInException() {
        super("로그인되지 않은 사용자입니다.");
    }

    public NotSignInException(String message) {
        super(message);
    }
}
