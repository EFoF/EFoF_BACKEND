package com.service.surveyservice.domain.member.exception.exceptions.member;

public class DuplicatedEmailException extends RuntimeException{
    public DuplicatedEmailException() {
        super("이미 다른 사용자가 사용 중인 이메일입니다.");
    }

    public DuplicatedEmailException(String message) {
        super(message);
    }
}
