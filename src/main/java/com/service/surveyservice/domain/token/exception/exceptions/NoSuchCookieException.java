package com.service.surveyservice.domain.token.exception.exceptions;

public class NoSuchCookieException extends RuntimeException{
    public NoSuchCookieException() {
        super("해당하는 쿠키가 존재하지 않습니다.");
    }

    public NoSuchCookieException(String message) {
        super(message);
    }
}
