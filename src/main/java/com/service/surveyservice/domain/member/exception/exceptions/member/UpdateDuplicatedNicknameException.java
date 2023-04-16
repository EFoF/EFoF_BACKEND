package com.service.surveyservice.domain.member.exception.exceptions.member;

public class UpdateDuplicatedNicknameException extends IllegalArgumentException{
    public UpdateDuplicatedNicknameException() {
        super("변경하려는 닉네임이 현재 닉네임과 일치합니다.");
    }

    public UpdateDuplicatedNicknameException(String s) {
        super(s);
    }
}
