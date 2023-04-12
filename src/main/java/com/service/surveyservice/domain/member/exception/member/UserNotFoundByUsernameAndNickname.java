package com.service.surveyservice.domain.member.exception.member;

public class UserNotFoundByUsernameAndNickname extends IllegalArgumentException {
    public UserNotFoundByUsernameAndNickname() {
        super("해당 본명과 닉네임으로 존재하는 유저를 찾을 수 없습니다.");
    }
    public UserNotFoundByUsernameAndNickname(String s) {
        super(s);
    }
}
