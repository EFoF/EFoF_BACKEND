package com.service.surveyservice.domain.member.exception.member;

public class NotMatchingUpdatingNicknameException extends IllegalArgumentException{
    public NotMatchingUpdatingNicknameException() {
        super("해당 이메일을 통해 찾은 사용자의 닉네임이 전달받은 닉네임과 일치하지 않습니다.");
    }

    public NotMatchingUpdatingNicknameException(String s) {
        super(s);
    }
}
