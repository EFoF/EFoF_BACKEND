package com.service.surveyservice.domain.member.exception.exceptions.member;

public class UserNotFoundException extends IllegalArgumentException{
    public UserNotFoundException() {
        super("해당 사용자를 찾을 수 없습니다.");
    }

    public UserNotFoundException(String s) {
        super(s);
    }
}
