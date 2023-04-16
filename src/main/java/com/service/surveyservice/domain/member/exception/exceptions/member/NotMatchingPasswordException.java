package com.service.surveyservice.domain.member.exception.exceptions.member;

public class NotMatchingPasswordException extends IllegalArgumentException{
    public NotMatchingPasswordException() {
        super("해당 이메일을 통해 찾은 사용자의 비밀번호가 전달받은 비밀번호와 일치하지 않습니다.");
    }

    public NotMatchingPasswordException(String s) {
        super(s);
    }
}
