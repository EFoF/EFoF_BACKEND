package com.service.surveyservice.domain.member.exception.exceptions.member;

public class InvalidEmailAndPasswordRequestException extends RuntimeException{
    public InvalidEmailAndPasswordRequestException() {
        super("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    public InvalidEmailAndPasswordRequestException(String message) {
        super(message);
    }
}
