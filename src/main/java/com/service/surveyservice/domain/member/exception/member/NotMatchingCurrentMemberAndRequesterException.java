package com.service.surveyservice.domain.member.exception.member;

public class NotMatchingCurrentMemberAndRequesterException extends IllegalArgumentException{
    public NotMatchingCurrentMemberAndRequesterException() {
        super("요청자와 현재 사용자가 일치하지 않습니다.");
    }

    public NotMatchingCurrentMemberAndRequesterException(String s) {
        super(s);
    }
}
