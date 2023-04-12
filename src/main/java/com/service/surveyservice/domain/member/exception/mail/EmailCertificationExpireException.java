package com.service.surveyservice.domain.member.exception.mail;

public class EmailCertificationExpireException extends IllegalArgumentException{
    public EmailCertificationExpireException() {
        super("이메일 인증 코드가 만료되었습니다.");
    }

    public EmailCertificationExpireException(String s) {
        super(s);
    }

}
