package com.service.surveyservice.domain.member.exception.member;

public class UpdateDuplicatedPasswordException extends IllegalArgumentException{
    public UpdateDuplicatedPasswordException() {
        super("변경하려는 비밀번호가 기존 비밀번호와 동일합니다.");
    }

    public UpdateDuplicatedPasswordException(String s) {
        super(s);
    }
}
