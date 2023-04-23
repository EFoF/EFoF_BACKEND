package com.service.surveyservice.domain.member.exception.exceptions.member;

public class DuplicatedSignUpException extends IllegalArgumentException {
    public DuplicatedSignUpException() {
        super("이미 계정이 연동된 회원입니다.");
    }

    public DuplicatedSignUpException(String s) {
        super(s);
    }
}
